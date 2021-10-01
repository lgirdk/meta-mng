/*****************************************************************************
 * Copyright 2021 Liberty Global B.V.
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
 ****************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <net/if.h>
#include <sys/socket.h>
#include <sys/ioctl.h>

#ifndef SYSDESC
#define SYSDESC "DOCSIS Cable Modem Gateway Device <<HW_REV: 1.2; VENDOR: Liberty Global; SW_REV: 3.4; MODEL: ABC3000>>"
#endif

#ifndef PROPERTY
#define PROPERTY "Virgin Media; United Kingdom"
#endif

static const char *interfaces[] = {
    "erouter0",
    "cm0",
    "wlan0",    /* Not expected to be found on a real device, but maybe useful for testing */
};

static int skta_get_mac_address (char *macstring, const char *device)
{
    struct ifreq ifr;
    int result = -1;
    int fd;

    if ((fd = socket (AF_INET, SOCK_DGRAM, 0)) < 0)
        return -1;

    memset (&ifr, 0, sizeof(struct ifreq));
    /*
       ifr.ifr_name doesn't need to be nul terminated, so strncpy() alone
       would be fine here. The compiler doesn't know that though, so ensure
       a nul terminated string to avoid compiler warnings...
    */
    strncpy (ifr.ifr_name, device, IFNAMSIZ - 1);
    ifr.ifr_name[IFNAMSIZ - 1] = 0;
    if (ioctl (fd, SIOCGIFHWADDR, &ifr) == 0) {
        unsigned char *mac = (unsigned char *) ifr.ifr_hwaddr.sa_data;
        snprintf (macstring, 18, "%02x:%02x:%02x:%02x:%02x:%02x", mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);
        result = 0;
    }

    close (fd);

    return result;
}

static int skta_get_counters (unsigned long long *counters, const char *device)
{
    FILE *fp;
    char buf[512];
    int device_len;
    int result = -1;

    if ((fp = fopen ("/proc/net/dev", "r")) == NULL)
        return -1;
    if (fgets (buf, sizeof(buf), fp) == NULL)
        goto done;
    if (fgets (buf, sizeof(buf), fp) == NULL)
        goto done;

    device_len = strlen (device);

    while (fgets (buf, sizeof(buf), fp) != NULL) {
        char *p = buf;
        while ((*p == ' ') || (*p == '\t'))
            p++;
        if (strncmp (p, device, device_len) != 0)
            continue;
        p += device_len;
        if (*p++ != ':')
            continue;
        /*
           counters[0] : tx_packets
           counters[1] : rx_packets
           counters[2] : tx_bytes
           counters[3] : rx_bytes
        */
        if (sscanf (p, "%llu%llu%*u%*u%*u%*u%*u%*u%llu%llu", &counters[3], &counters[1], &counters[2], &counters[0]) == 4)
            result = 0;
        break;
    }

done:
    fclose (fp);

    return result;
}

static int skta_get_system_description (char *buf, size_t len)
{
    snprintf (buf, len, SYSDESC);

    return 0;
}

static int skta_get_property (char *buf, size_t len)
{
    FILE *file;
    char *pos = NULL;

    if ((file = popen ("syscfg get skproperty ", "r")) != NULL) {
        pos = fgets (buf, len, file);
        pclose (file);
    }

    if (pos) {
        len = strlen (pos);
        if (len > 0) {
            if (pos[len - 1] == '\n')
                pos[len - 1] = 0;
            return 0;
        }
    }

    snprintf (buf, len, PROPERTY);

    return 0;
}

int main (int argc, char* argv[])
{
    char interface[IFNAMSIZ];
    char macstring[18];
    char sysdesc[200];
    char property[64];
    unsigned long long counters[4];     /* tx_packets, rx_packets, tx_bytes and rx_bytes */
    int i;

    for (i = 0; i < sizeof(interfaces)/sizeof(interfaces[0]); i++) {
        strncpy (interface, interfaces[i], sizeof(interface) - 1);
        interface[sizeof(interface) - 1] = 0;
        if (skta_get_mac_address (macstring, interface) == 0)
            break;
    }

    if (i >= sizeof(interfaces)/sizeof(interfaces[0])) {
        fprintf (stderr, "Error detecting interface (tried:");
        for (i = 0; i < sizeof(interfaces)/sizeof(interfaces[0]); i++)
            fprintf (stderr, " %s", interfaces[i]);
        fprintf (stderr, ")\n");
        return 1;
    }

    if (skta_get_counters (counters, interface) != 0) {
        fprintf (stderr, "Error reading counters for '%s'\n", interface);
        return 1;
    }

    if (skta_get_system_description (sysdesc, sizeof(sysdesc)) != 0) {
        fprintf (stderr, "Error reading System Description\n");
        return 1;
    }

    if (skta_get_property (property, sizeof(property)) != 0) {
        fprintf (stderr, "Error reading Property\n");
        return 1;
    }

    /*
       Convert hex chars in MAC address to upper case.
    */
    for (i = 0; i < sizeof(macstring); i++) {
        if (macstring[i] == 0)
            break;
        if ((macstring[i] >= 'a') && (macstring[i] <= 'f'))
            macstring[i] -= ('a' - 'A');
    }

    printf ("CM MAC Address: %s\n"
            "Docsis Packet Up Count: %llu\n"
            "Docsis Packet Down Count: %llu\n"
            "Docsis Byte Up Count: %llu\n"
            "Docsis Byte Down Count: %llu\n"
            "SysDesc: %s\n"
            "Property: %s\n",
            macstring,
            counters[0], counters[1], counters[2], counters[3],
            sysdesc,
            property);

    return 0;
}

