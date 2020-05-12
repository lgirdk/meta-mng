
# Disable the xz PACKAGECONFIG option (ie xz compression for journald ?) to try
# to save space (it may be the only thing in the final image which depends on
# liblzma).

PACKAGECONFIG_remove = "xz"
