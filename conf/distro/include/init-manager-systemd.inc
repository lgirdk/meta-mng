
# Use systemd for system initialization

VIRTUAL-RUNTIME_dev_manager = "udev"
VIRTUAL-RUNTIME_login_manager = "busybox"
VIRTUAL-RUNTIME_init_manager = "systemd"
VIRTUAL-RUNTIME_initscripts = "systemd-compat-units"

DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"

DISTRO_FEATURES_append = " systemd"

# Avoid pulling in the udev static hwdb by default (note that udev-hwdb is provided by systemd)
BAD_RECOMMENDATIONS += "udev-hwdb"
