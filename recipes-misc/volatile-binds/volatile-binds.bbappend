
# The default value for VOLATILE_BINDS handles making /var/lib, /var/cache,
# /var/spool and /srv writeable in an otherwise read-only rootfs. For
# OneFirmware none of these are required (see the earlier change to fstab to
# mount tmpfs over the entire /var directory), so over-ride the default value
# of VOLATILE_BINDS.

# Note that volatile-binds is systemd specific, so even if we did need to
# create a volatile bind mount to make a file or directory writeable it should
# be done in a way which is compatible with both systemd and non-systemd builds
# (e.g. with calls to mount --bind from within an init script etc) and NOT rely
# on volatile-binds.

VOLATILE_BINDS = ""
