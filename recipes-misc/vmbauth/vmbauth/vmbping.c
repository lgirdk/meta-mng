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

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <poll.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netinet/ip_icmp.h>
#include <asm/byteorder.h>
#include <net/if.h>
#include <time.h>
#include <errno.h>

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
			printf("poll(): %s\n", strerror(errno));
			return -1;
		}

		if (rc == 0)
			return 0;

		if (pfd.revents & POLLERR) {
			printf("error on socket\n");
			return -1;
		}

		if (pfd.revents & POLLIN) {
			rc = recv(s, _resp, sizeof(_resp), MSG_DONTWAIT);
			if (rc < 0) {
				if (errno == EAGAIN || errno == EWOULDBLOCK)
					continue;
				printf("recv(): %s\n", strerror(errno));
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

int main(int argc, char *argv[])
{
	int s;
	char _reqbuf[sizeof(struct icmp) + ICMP_PAYLOAD_LEN];
	struct icmp *req = (struct icmp *)_reqbuf;
	struct sockaddr_in dst;
	int rc;
	int timeout;
	struct timespec expires;
	int timeout_cnt;
	int max_timeouts;
	const char *iface;
	struct ifreq ifr;
	int seq = 0;

	memset(&dst, 0, sizeof(dst));
	dst.sin_family = AF_INET;
	if (!inet_pton(AF_INET, argv[1], &dst.sin_addr)) {
		printf("inet_pton(%s): %s\n", argv[1], strerror(errno));
		exit(-1);
	}

	iface = argv[2];
	timeout = atoi(argv[3]);
	max_timeouts = atoi(argv[4]);

	s = socket(AF_INET, SOCK_RAW, IPPROTO_ICMP);
	if (s < 0) {
		printf("socket(): %s\n", strerror(errno));
		exit(-1);
	}

	memset(&ifr, 0, sizeof(ifr));
	strncpy(ifr.ifr_name, iface, IFNAMSIZ);
	rc = setsockopt(s, SOL_SOCKET, SO_BINDTODEVICE, &ifr, sizeof(ifr));
	if (rc) {
		printf("setsockopt(SO_BINDTODEVICE) to %s failed: %s\n", iface, strerror(errno));
		exit(-1);
	}

	memset(req, 0, sizeof(*req) + ICMP_PAYLOAD_LEN);
	req->icmp_type = ICMP_ECHO;
	req->icmp_id = getpid();

	clock_gettime(CLOCK_MONOTONIC, &expires);

	timeout_cnt = 0;

	for (;;) {
		seq++;
		req->icmp_seq = htons(seq);
		req->icmp_cksum = 0;
		req->icmp_cksum = inet_cksum(req, ICMP_MINLEN + ICMP_PAYLOAD_LEN);
		rc = sendto(s, req, ICMP_MINLEN + ICMP_PAYLOAD_LEN, 0, (void *)&dst, sizeof(dst));
		if (rc < 0) {
			printf("sendto(): %s\n", strerror(errno));
			exit(-1);
		}

		expires.tv_sec += timeout;

		rc = icmp_reply_recvtimeout(s, req->icmp_id, &expires);
		if (rc < 0) {
			printf("err on ICMP wait\n");
			exit(-1);
		}
		if (rc == 0) {
			timeout_cnt++;
			if (timeout_cnt >= max_timeouts) {
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
