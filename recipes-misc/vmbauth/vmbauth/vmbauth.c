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
/*
 * vmbauth.c file is derived from radcli example file: https://github.com/radcli/radcli/blob/master/src/radexample.c
 *
 * Copyright (C) 1995,1996,1997 Lars Fenneberg
 * Copyright (C) 2015 Nikos Mavrogiannopoulos
 *
 * See the radcli COPYRIGHT file for the respective terms and conditions.
 */

#include <stdio.h>
#include <string.h>
#include <ctype.h>

#include <netinet/in.h>
#include <arpa/inet.h>

#include <syscfg/syscfg.h>
#include <radcli/radcli.h>

static FILE *logfile = NULL;

static int get_iface_ip(const char *iface, struct in_addr *ip)
{
	FILE *fp;
	char cmd[128];
	char ipaddress[128];

	snprintf(cmd, sizeof(cmd), "ip -4 a l dev %s | awk '$1==\"inet\"{ print $2 }'  | awk -F / '{print $1}'", iface);
	fp = popen(cmd, "r");
	if (!fp) {
		fprintf(logfile, "%s ip query failed\n", iface);
		return -1;
	}
	if (!fgets(ipaddress, sizeof(ipaddress), fp)) {
		fprintf(logfile, "%s ip query failed\n", iface);
		return -1;
	}
	pclose(fp);

	inet_aton(ipaddress, ip);
	ip->s_addr = ntohl(ip->s_addr);
	return 0;
}

static int get_iface_mac(const char *iface, char *mac, unsigned int macbuflen)
{
	char path[128];
	FILE *fp;
	int i;

	snprintf(path, sizeof(path), "/sys/class/net/%s/address", iface);
	fp = fopen(path, "r");
	if (!fp) {
		fprintf(logfile, "%s does not exist\n", iface);
		return -1;
	}
	if (!fgets(mac, macbuflen, fp)) {
		fprintf(logfile, "%s mac failure\n", iface);
		fclose(fp);
		return -1;
	}
	fclose(fp);

	for (i = 0; mac[i] && !isspace(mac[i]); i++)
		mac[i] = (mac[i] == ':') ? '-' : toupper(mac[i]);
	mac[i] = '\0';

	return 0;
}

int main (int argc, char *argv[])
{
	int             result;
	VALUE_PAIR 	*send, *received;
	uint32_t	service;
	rc_handle	*rh;
	struct in_addr nas_ip;
	char		username[128];
	char		password[128];
	char		wan_mac[128];
	char		gre_mac[128];
	const char *wanif = argv[3];
	const char *greif = argv[4];

	if ((rh = rc_read_config(argv[1])) == NULL)
		return ERROR_RC;

	if ((logfile = fopen(argv[2], "a")) == NULL)
		return ERROR_RC;

	send = NULL;

	username[0] = 0;
	syscfg_get(NULL, "tunneled_static_ip_username", username, sizeof(username));
	if (!username[0]) {
		fprintf(logfile, "username is not configured\n");
		return ERROR_RC;
	}
	if (rc_avpair_add(rh, &send, PW_USER_NAME, username, -1, 0) == NULL)
		return ERROR_RC;

	password[0] = 0;
	syscfg_get(NULL, "tunneled_static_ip_password", password, sizeof(password));
	if (!password[0]) {
		fprintf(logfile, "password is not configured\n");
		return ERROR_RC;
	}
	if (rc_avpair_add(rh, &send, PW_USER_PASSWORD, password, -1, 0) == NULL)
		return ERROR_RC;

	if (get_iface_mac(wanif, wan_mac, sizeof(wan_mac))) {
		fprintf(logfile, "get_iface_mac(%s) failed\n", wanif);
		return ERROR_RC;
	}
	if (get_iface_mac(greif, gre_mac, sizeof(gre_mac))) {
		fprintf(logfile, "get_iface_mac(%s) failed\n", greif);
		return ERROR_RC;
	}
	if (rc_avpair_add(rh, &send, PW_CALLED_STATION_ID, wan_mac, -1, 0) == NULL)
		return ERROR_RC;

	if (rc_avpair_add(rh, &send, PW_CALLING_STATION_ID, gre_mac, -1, 0) == NULL)
		return ERROR_RC;

	if (get_iface_ip(wanif, &nas_ip)) {
		fprintf(logfile, "get_iface_ip(%s) failed\n", wanif);
		return ERROR_RC;
	}
	if (rc_avpair_add(rh, &send, PW_NAS_IP_ADDRESS, &nas_ip, -1, 0) == NULL)
		return ERROR_RC;

	result = rc_aaa(rh, 0, send, &received, NULL, 0, PW_ACCESS_REQUEST);

	if (result == OK_RC) {
		VALUE_PAIR *vp = received;
		char name[128];
		char value[128];

		fprintf(logfile, "RADIUS Authentication OK\n");

		while(vp != NULL) {
			if (rc_avpair_tostr(rh, vp, name, sizeof(name), value, sizeof(value)) == 0)
				fprintf(logfile, "%s=%s\n", name, value);
			vp = vp->next;
		}
		fflush(logfile);
		//system("sysevent set vmb-mode start");
		system("vmb-mode.sh restart >> /tmp/vmb-radius-client/vmb-mode.log 2>&1");
	} else {
		fprintf(logfile, "RADIUS Authentication failure (RC=%i)\n", result);
	}

	fclose(logfile);

	return result;
}
