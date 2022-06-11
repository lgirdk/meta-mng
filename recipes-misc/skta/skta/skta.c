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
#include <sys/stat.h>
#include <fcntl.h>
#include <ctype.h>

#ifndef SYSDESC
#if defined (FEATURE_GPON)
#define SYSDESC "Fiber Gateway Device <<HW_REV: 1.2; VENDOR: Liberty Global; SW_REV: 3.4; MODEL: ABC3000>>"
#else
#define SYSDESC "DOCSIS Cable Modem Gateway Device <<HW_REV: 1.2; VENDOR: Liberty Global; SW_REV: 3.4; MODEL: ABC3000>>"
#endif
#endif

#ifndef PROPERTY
#define PROPERTY "Virgin Media; United Kingdom"
#endif

static const char *interfaces[] = {
#if defined(_LG_MV2_PLUS_)
    "lbr0",
#else
    "erouter0",
    "cm0",
    "wlan0",    /* Not expected to be found on a real device, but maybe useful for testing */
#endif
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

#if defined(_LG_MV2_PLUS_)

static int validate_mac (char *macstring)
{
    int i;

    for (i = 0; i < 6; i++)
    {
        if ((isxdigit(macstring[0])) &&
            (isxdigit(macstring[1])) &&
            (macstring[2] == ((i == 5) ? 0 : ':')))
        {
            macstring += 3;
        }
        else
        {
            return -1;
        }
    }

    return 0;
}

static int skta_get_mac_address_via_syscfg (char *macstring)
{
    FILE *fp;
    char *cmd = "syscfg get cm_mac";
    int result = -1;

    fp = popen (cmd, "r");

    if (fp == NULL)
        return -1;

    if (fgets (macstring, 18, fp) != NULL)
        if (validate_mac (macstring) == 0)
            result = 0;

    pclose (fp);

    return result;
}

static int skta_get_mac_address_via_dmcli (char *macstring)
{
    FILE *fp;
    char *cmd = "dmcli eRT retv Device.DeviceInfo.X_LGI-COM_CM_MAC";
    int result = -1;

    fp = popen (cmd, "r");

    if (fp == NULL)
        return -1;

    if (fgets (macstring, 18, fp) != NULL)
        if (validate_mac (macstring) == 0)
            result = 0;

    pclose (fp);

    return result;
}

static int skta_get_mac_address_via_snmp (char *macstring)
{
    FILE *fp;
    char buf[128];
    char *cmd = "snmpget -cpub -v2c -Ov 172.31.255.45 1.3.6.1.2.1.2.2.1.6.2";
    int result = -1;

    fp = popen (cmd, "r");

    if (fp == NULL)
        return -1;

    if (fgets (buf, sizeof(buf), fp) != NULL) {
        int len = strlen (buf);
        if ((len == 26) && (buf[25] == '\n') && (memcmp (buf, "STRING: ", 8) == 0)) {
            memcpy (macstring, buf + 8, 17);
            macstring[17] = 0;
            if (validate_mac (macstring) == 0)
                result = 0;
        }
    }

    pclose (fp);

    return result;
}

#endif

#if defined (FEATURE_GPON)

static int skta_get_serial_number_via_dmcli (char *buf, size_t len)
{
    FILE *fp;
    char *cmd = "dmcli eRT retv Device.DeviceInfo.SerialNumber";
    int result = -1;

    fp = popen (cmd, "r");

    if (fp == NULL)
        return -1;

    if (fgets (buf, len, fp) != NULL) {
        len = strlen(buf);
        if (len > 0) {
            if (buf[len - 1] == '\n') {
                buf[len - 1] = 0;
                len--;
            }
            if (len > 0) {
                result = 0;
            }
        }
    }

    pclose (fp);

    return result;
}

#endif

static int skta_sync_counters (void)
{
#if defined(_LG_MV2_PLUS_)
    unsigned char buf[4096];
    int fd;

    /*
       Read from /proc/net/nf_conntrack_offload to sync counters
    */
    fd = open ("/proc/net/nf_conntrack_offload", O_RDONLY);

    if (fd == -1)
        return -1;

    while (1) {
        if (read (fd, buf, sizeof(buf)) <= 0)
            break;
    }

    close (fd);
#endif

    return 0;
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
#if defined (FEATURE_GPON)
    char serialnumber[64];
#endif
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

#if defined(_LG_MV2_PLUS_)
    /*
       At this point, macstring holds the MAC address of the interface from
       which byte/packet counters will be read. However we need the MAC address
       of the CM interface, which on Mv2+ is different.

       Try 3 different approaches to read the CM MAC address: syscfg is fast
       but will fail if the cache is empty. dmcli is slow but seems to be the
       most reliable. snmpget is in between and mostly just kept as a reference
       (it has been seen to fail sometimes if called too early during startup).
       Since dmcli also ends up calling snmpget (but indirectly via the HAL) it
       may not actually be any more reliable. To be investigated...

       It's possible for all three calls below to fail if we are running too
       soon during startup and no value has been previously saved to syscfg.
       Since returning the wromg MAC address causes various problems, include a
       sanity check and abort if the CM MAC address is not successfully read.
    */

    macstring[0] = 0;

    if ((skta_get_mac_address_via_syscfg (macstring) != 0) &&
        (skta_get_mac_address_via_dmcli (macstring) != 0) &&
        (skta_get_mac_address_via_snmp (macstring) != 0))
    {
        fprintf (stderr, "Error reading CM MAC address\n");
        return 1;
    }
#endif


#if defined (FEATURE_GPON)
    if (skta_get_serial_number_via_dmcli (serialnumber, sizeof(serialnumber)) != 0) {
        fprintf (stderr, "Error reading Serial Number\n");
        return 1;
    }
#endif

    skta_sync_counters();

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
#if defined (FEATURE_GPON)
            serialnumber,
#else
            macstring,
#endif
            counters[0], counters[1], counters[2], counters[3],
            sysdesc,
            property);

    return 0;
}

