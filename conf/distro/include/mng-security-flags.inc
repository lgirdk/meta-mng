# ----------------------------------------------------------------------------
#
# Enable extra CFLAGS which have security benefits.
#
# For reference, see:
#
#   - https://wiki.debian.org/Hardening
#   - https://idea.popcount.org/2013-08-15-fortify_source/
#   - https://outflux.net/blog/archives/2014/01/27/fstack-protector-strong
#
# ----------------------------------------------------------------------------

# The meta-oe and meta-virtualization layer config files include over-rides
# which force SECURITY_CFLAGS to SECURITY_NO_PIE_CFLAGS, so even though we
# don't use it directly here, SECURITY_NO_PIE_CFLAGS still needs to be defined.

SECURITY_NO_PIE_CFLAGS = "${@oe.utils.conditional('DEBUG_BUILD','1','','-D_FORTIFY_SOURCE=2',d)}"

SECURITY_CFLAGS = "${SECURITY_NO_PIE_CFLAGS}"

TARGET_CFLAGS_append_class-target = " ${SECURITY_CFLAGS}"

# ----------------------------------------------------------------------------
