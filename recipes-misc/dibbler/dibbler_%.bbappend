
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://always-use-erouter-for-DUID-generation.patch"

# ----------------------------------------------------------------------------

# Dibbler is written in C++, therefore by default the dibbler packages all have
# a runtime dependency on libstdc++ (a ~1MB library on the target).
#
# If only one package in the final rootfs requires libstdc++ then linking that
# package statically with libstdc++ can save some space in the final rootfs
# image. If multiple packages need libstdc++ the linking them all dynamically
# with libstdc++ may be better.

# CPPFLAGS += "-ffunction-sections -fdata-sections"
# LDFLAGS += "-Wl,--gc-sections -static-libstdc++"

# ----------------------------------------------------------------------------
