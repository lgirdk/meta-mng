
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Disable lzma support to save space. There's no configure option, so
# CMakeLists.txt needs to be patched (by default the dependency on liblzma
# is optional but enabled if found).

DEPENDS_remove = "xz"

SRC_URI += "file://0001-disable-liblzma-dependency.patch;patchdir=../../"
