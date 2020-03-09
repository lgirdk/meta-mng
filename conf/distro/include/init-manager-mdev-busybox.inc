
# Use busybox/mdev for system initialization

VIRTUAL-RUNTIME_dev_manager = "busybox-mdev"
VIRTUAL-RUNTIME_login_manager = "busybox"
VIRTUAL-RUNTIME_init_manager = "busybox"
VIRTUAL-RUNTIME_initscripts = "initscripts"

DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"
