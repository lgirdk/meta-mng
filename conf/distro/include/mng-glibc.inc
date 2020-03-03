
# ----------------------------------------------------------------------------

# DISTRO_FEATURES_append_arm = " ld-is-gold"
# DISTRO_FEATURES_append_armeb = " ld-is-gold"
# DISTRO_FEATURES_append_x86 = " ld-is-gold"

# ----------------------------------------------------------------------------

# Prefer to use the RPC library bundled with glibc when possible (ie upto
# glibc 2.27 in OE 2.5, after that the glibc recipe in oe-core disables RPC
# support by default). If glibc doesn't provide libucrpc then the standalone
# libucrpc will be used instead.

PREFERRED_PROVIDER_libucrpc = "glibc"

# ----------------------------------------------------------------------------
