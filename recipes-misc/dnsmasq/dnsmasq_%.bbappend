
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://0001-add-ACS-discovery-and-client-DB-WIP.patch"
SRC_URI += "file://0001-include-fingerprint-information-in-dhcp-server-lease.patch"
SRC_URI += "file://0001-add-support-DNS-failover-for-not-implement-error-cod.patch"
SRC_URI += "file://0001-DAD-mechanism-behavior-before-replying-with-DHCP-ACK.patch"
SRC_URI += "file://0002-DAD-mechanism-before-replying-with-DHCP-Offer.patch"

# ----------------------------------------------------------------------------

# Disable rtc support for embedded systems which don't have an RTC
# which keeps time over reboots. Causes dnsmasq to use uptime
# for timing, and keep lease lengths rather than expiry times
# in its leases file. This also make dnsmasq "flash disk friendly".
# Normally, dnsmasq tries very hard to keep the on-disk leases file
# up-to-date: rewriting it after every renewal.  When HAVE_BROKEN_RTC 
# is in effect, the lease file is only written when a new lease is 
# created, or an old one destroyed. (Because those are the only times 
# it changes.) This vastly reduces the number of file writes, and makes
# it viable to keep the lease file on a flash filesystem.
#
# Unfortunately, there is code within RDKB which expects to parse the
# dnsmasq leases file to support Device.Hosts.Host.1.LeaseTimeRemaining
# etc, so we can't make use of this option. Fixme: to be reviewed.

# PACKAGECONFIG_remove = "rtc"

# ----------------------------------------------------------------------------

# The tftp server is used to support netboot and is not required for RDKB

CFLAGS += "-DNO_TFTP"

# ----------------------------------------------------------------------------

# By default the Puma6 kernel config has inotify support disabled. However, for
# mng we have a kernel config change to enable it.

# CFLAGS += "-DNO_INOTIFY"

# ----------------------------------------------------------------------------

INHIBIT_UPDATERCD_BBCLASS = "1"

SYSTEMD_AUTO_ENABLE = "disable"
