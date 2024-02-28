/*********************************************************************************
 * Copyright 2022 Liberty Global B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *********************************************************************************/

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <getopt.h>
#include <poll.h>
#include <sys/types.h>
#include <inttypes.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netinet/ip_icmp.h>
#include <asm/byteorder.h>
#include <net/if.h>
#include <time.h>
#include <errno.h>
#include <err.h>

static uint16_t inet_cksum(void *_buf, size_t n)
{
	uint16_t *buf = _buf;
	unsigned long sum = 0;

	while (n > 1) {
		sum += *buf++;
		n -= 2;
	}

	if (n) {
		sum += __le16_to_cpu(*(uint8_t *)buf);
	}

	sum = (sum & 0xffff) + (sum >> 16);
	sum = (sum & 0xffff) + (sum >> 16);
	return (uint16_t)~sum;
}

static uint64_t get_iface_rx_counter(const char *iface)
{
	FILE *fp;
	char buffer[256];
	uint64_t counter;

	/*
	 * mv2+ packet acceleration workaround:
	 * Reading "/proc/net/nf_conntrack_offload" triggers an update of the interface counters
	 */
	fp = fopen("/proc/net/nf_conntrack_offload_update_stats", "r");
	if (!fp)
		fp = fopen("/proc/net/nf_conntrack_offload", "r");
	if (fp) {
		while (fread(buffer, 1, sizeof(buffer), fp) == sizeof(buffer));
		fclose(fp);
	}

	snprintf(buffer, sizeof(buffer), "/sys/class/net/%s/statistics/rx_packets", iface);
	fp = fopen(buffer, "r");
	if (!fp)
		return 0;
	counter = 0;
	if (fgets(buffer, sizeof(buffer), fp))
		sscanf(buffer, "%" PRIu64, &counter);
	fclose(fp);
	return counter;
}

static unsigned int get_active_flows_count(void)
{
	FILE *fp;
	char buffer[256], *p;
	unsigned int count = 100000; /* high default value just in case sth goes wrong */
	unsigned int c;

	fp = fopen("/proc/driver/flowmgr/fap", "r");
	if (!fp)
		return count;

	#define FLOW_CNT_STR "Active flows"
	while (fgets(buffer, sizeof(buffer), fp)) {
		if (!strncmp(buffer, FLOW_CNT_STR, sizeof(FLOW_CNT_STR) - 1)) {
			p = strchr(buffer + sizeof(FLOW_CNT_STR) - 1, ':');
			if (p && sscanf(p + 1, "%u", &c) == 1)
				count = c;
			break;
		}
	}
	#undef FLOW_CNT_STR

	fclose(fp);
	return count;
}

static int icmp_reply_recvtimeout(int s, uint16_t id, const struct timespec *expires)
{
	struct pollfd pfd = {
		.fd = s,
		.events = POLLIN | POLLERR,
	};
	struct timespec now;
	int rc, timeout_ms;
	char _resp[1024];
	struct icmp *resp;
	struct iphdr *iphdr = (struct iphdr *)_resp;

	for (;;) {
		clock_gettime(CLOCK_MONOTONIC, &now);
		if ((now.tv_sec > expires->tv_sec) || (now.tv_sec == expires->tv_sec && now.tv_nsec >= expires->tv_nsec))
			return 0;

		timeout_ms = (expires->tv_sec - now.tv_sec) * 1000;
		timeout_ms += (expires->tv_nsec - now.tv_nsec) / 1000000;

		rc = poll(&pfd, 1, timeout_ms ? timeout_ms : 1);
		if (rc < 0) {
			if (errno == EINTR)
				continue;
			warn("poll(): ");
			return -1;
		}

		if (rc == 0)
			return 0;

		if (pfd.revents & POLLERR) {
			warnx("error on socket");
			return -1;
		}

		if (pfd.revents & POLLIN) {
			rc = recv(s, _resp, sizeof(_resp), MSG_DONTWAIT);
			if (rc < 0) {
				if (errno == EAGAIN || errno == EWOULDBLOCK)
					continue;
				warn("recv(): ");
				return -1;
			}
			if (rc < sizeof(*iphdr))
				continue;
			if (rc < ntohs(iphdr->tot_len))
				continue;
			rc = ntohs(iphdr->tot_len);

			if (rc < (iphdr->ihl * 4))
				continue;
			resp = (struct icmp *)&_resp[iphdr->ihl * 4];
			rc -= iphdr->ihl * 4;

			if (rc < ICMP_MINLEN)
				continue;
			if (resp->icmp_type != ICMP_ECHOREPLY)
				continue;
			if (resp->icmp_id != id)
				continue;
			if (inet_cksum(resp, rc))
				continue;
			return rc;
		}
	}
}

#define ICMP_PAYLOAD_LEN (64 - ICMP_MINLEN)

static const struct option long_options[] = {
	{ "interval", required_argument, NULL, 'i' },
	{ "ntimeouts", required_argument, NULL, 'n' },
	{ "iface", required_argument, NULL, 'I' },
	{ "timeout", required_argument, NULL, 't' },
	{ "help", no_argument, NULL, 'h' },
	{ NULL, 0, NULL, 0 }
};

static long stolong_or_exit(const char *s)
{
	char *endp;
	long r;

	errno = 0;
	r = strtol(s, &endp, 0);
	if (errno == ERANGE)
		errx(EXIT_FAILURE, "Integer conversion failed: out of range");
	if (s == endp || (*s && *endp))
		errx(EXIT_FAILURE, "Integer conversion failed: invalid string: '%s'", s);
	return r;
}

static inline void timespec_sub(const struct timespec *a, const struct timespec *b, struct timespec *r)
{
	r->tv_sec = a->tv_sec - b->tv_sec;
	r->tv_nsec = a->tv_nsec - b->tv_nsec;
	if (r->tv_nsec < 0) {
		r->tv_sec--;
		r->tv_nsec += 1000000000;
	}
}

static inline int timespec_cmp(const struct timespec *a, const struct timespec *b)
{
	if (a->tv_sec != b->tv_sec)
		return a->tv_sec > b->tv_sec ? 1 : -1;
	if (a->tv_nsec != b->tv_nsec)
		return a->tv_nsec > b->tv_nsec ? 1 : -1;
	return 0;
}

static inline void get_polling_interval(struct timespec *i)
{
	unsigned int poll_interval; /* in 100ms */

	poll_interval = 1 + get_active_flows_count() / 100; /* interval in 100ms with maximum 10 seconds */
	if (poll_interval > 100)
		poll_interval = 100;
	i->tv_sec = poll_interval / 10;
	i->tv_nsec = (poll_interval % 10) * 100000000;
}

int main(int argc, char *argv[])
{
	int s;
	char _reqbuf[sizeof(struct icmp) + ICMP_PAYLOAD_LEN];
	struct icmp *req = (struct icmp *)_reqbuf;
	struct sockaddr_in dst;
	int rc;
	struct timespec expires, now, left, interval;
	int timeout_cnt;
	uint64_t rx_counter, last_rx_counter;
	int seq = 0;
	int opt;

	/* options */
	const char *opt_iface = NULL;
	long opt_interval = 10;
	long opt_ntimeouts = 3;
	long opt_timeout = 0;

	while ((opt = getopt_long(argc, argv, "i:n:I:t:h", long_options, NULL)) != -1) {
		switch (opt) {
		case 'i':
			opt_interval = stolong_or_exit(optarg);
			if (opt_interval <= 0)
				errx(EXIT_FAILURE, "interval must be a positive integer in seconds");
			break;
		case 'n':
			opt_ntimeouts = stolong_or_exit(optarg);
			if (opt_ntimeouts <= 0)
				errx(EXIT_FAILURE, "ntimeouts must be a positive integer specifying the number of consecutive ping attempts");
			break;
		case 'I':
			opt_iface = optarg;
			break;
		case 't':
			opt_timeout = stolong_or_exit(optarg);
			if (opt_timeout < 0)
				errx(EXIT_FAILURE, "timeout must be non-negative");
			break;
		case '?':
			errx(EXIT_FAILURE, "  Try: '%s --help' for help", program_invocation_short_name);
		case 'h':
			fprintf(stderr,
				"Usage: %s [options] host\n"
				"\n"
				"Options:\n"
				"  -i, --interval=<interval>     Interval in seconds between ICMP echo requests. Default: 10\n"
				"  -n, --ntimeouts=<count>       Number of consecutive ICMP requests without answer before triggering VMB reset. Default: 3\n"
				"  -I, --iface=<name>            Interface name to bind the socket. Default: none\n"
				"  -t, --timeout=<timeout>       Interface idle duration in seconds before starting to send ICMP echo requests. Default: 0\n"
				"                                A non-zero value requires an interface to be specified (-i, --iface).\n"
				"                                A non-zero value also changes the behavior in that we no longer watch for\n"
				"                                ICMP echo responses alone to our requests and any received packet will indicate link activity.\n"
				"  -h, --help                    This help message.\n"
				, program_invocation_short_name);
			exit(EXIT_FAILURE);
		}
	}
	if (optind >= argc)
		errx(EXIT_FAILURE, "Target host is not specified");
	if (opt_timeout > 0 && opt_iface == NULL)
		errx(EXIT_FAILURE, "Idle timeout parameter also requires an interface to be specified (-i, --iface)");

	memset(&dst, 0, sizeof(dst));
	dst.sin_family = AF_INET;
	if (!inet_pton(AF_INET, argv[optind], &dst.sin_addr))
		err(EXIT_FAILURE, "inet_pton(%s): ", argv[optind]);

	s = socket(AF_INET, SOCK_RAW, IPPROTO_ICMP);
	if (s < 0)
		err(EXIT_FAILURE, "socket(): ");

	if (opt_iface) {
		rc = setsockopt(s, SOL_SOCKET, SO_BINDTODEVICE, opt_iface, strlen(opt_iface) + 1);
		if (rc)
			err(EXIT_FAILURE, "setsockopt(SO_BINDTODEVICE) to %s failed: ", opt_iface);
	}

	memset(req, 0, sizeof(*req) + ICMP_PAYLOAD_LEN);
	req->icmp_type = ICMP_ECHO;
	req->icmp_id = getpid();

	clock_gettime(CLOCK_MONOTONIC, &expires);
	if (opt_timeout) {
		expires.tv_sec += opt_timeout;
		setsockopt(s, SOL_SOCKET, SO_RCVBUF, (int[]){0}, sizeof(int));
	}

	timeout_cnt = 0;
	last_rx_counter = 0;

	for (;;) {
		if (opt_timeout) {
			get_polling_interval(&interval);
			rx_counter = get_iface_rx_counter(opt_iface);
			if (rx_counter != last_rx_counter) {
				last_rx_counter = rx_counter;
				timeout_cnt = 0;
				clock_gettime(CLOCK_MONOTONIC, &expires);
				expires.tv_sec += opt_timeout;
				clock_nanosleep(CLOCK_MONOTONIC, 0, &interval, NULL);
				continue;
			}
			clock_gettime(CLOCK_MONOTONIC, &now);
			timespec_sub(&expires, &now, &left);
			if (left.tv_sec >= 0) {
				if (timespec_cmp(&left, &interval) >= 0)
					left = interval;
				clock_nanosleep(CLOCK_MONOTONIC, 0, &left, NULL);
				continue;
			}
			if (timeout_cnt++ >= opt_ntimeouts) {
				printf("Max timeouts reached. Restarting the VMB tunnel...\n");
				system("vmbauth.sh");
				exit(-1);
			}
			expires = now;
		}

		seq++;
		req->icmp_seq = htons(seq);
		req->icmp_cksum = 0;
		req->icmp_cksum = inet_cksum(req, ICMP_MINLEN + ICMP_PAYLOAD_LEN);
		rc = sendto(s, req, ICMP_MINLEN + ICMP_PAYLOAD_LEN, 0, (void *)&dst, sizeof(dst));
		if (rc < 0)
			err(EXIT_FAILURE, "sendto(): ");

		expires.tv_sec += opt_interval;

		if (opt_timeout)
			continue;

		rc = icmp_reply_recvtimeout(s, req->icmp_id, &expires);
		if (rc < 0)
			errx(EXIT_FAILURE, "err on ICMP wait");

		if (rc == 0) {
			timeout_cnt++;
			if (timeout_cnt >= opt_ntimeouts) {
				printf("Max timeouts reached. Restarting the VMB tunnel...\n");
				system("vmbauth.sh");
				exit(-1);
			}
			continue;
		}
		if (rc > 0) {
			timeout_cnt = 0;
			/* sleep for the rest */
			while (clock_nanosleep(CLOCK_MONOTONIC, TIMER_ABSTIME, &expires, NULL));
		}
	}

	return 0;
}
