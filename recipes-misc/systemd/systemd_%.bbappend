
# Disable the xz PACKAGECONFIG option (ie xz compression for journald ?) to try
# to save space (it may be the only thing in the final image which depends on
# liblzma).

PACKAGECONFIG_remove = "xz"

# We don't need systemd to be involved in running fsck, so remove util-linux
# fsck from RRECOMMENDS to save space.

RRECOMMENDS_${PN}_remove = "util-linux-fsck"
