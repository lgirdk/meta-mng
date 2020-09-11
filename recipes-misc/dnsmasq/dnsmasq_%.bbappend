
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://0001-add-ACS-discovery-and-client-DB-WIP.patch"

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

PACKAGECONFIG_remove = "rtc"

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
