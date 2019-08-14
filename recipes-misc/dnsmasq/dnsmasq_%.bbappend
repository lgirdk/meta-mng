
# ----------------------------------------------------------------------------

# By default the Puma6 kernel config has inotify support disabled. However, for
# mng we have a kernel config change to enable it.

# CFLAGS += "-DNO_INOTIFY"

# ----------------------------------------------------------------------------

INHIBIT_UPDATERCD_BBCLASS = "1"

SYSTEMD_AUTO_ENABLE = "disable"
