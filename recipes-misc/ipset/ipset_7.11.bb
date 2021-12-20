SUMMARY = "Administration tool for IP sets"
HOMEPAGE = "https://ipset.netfilter.org"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

DEPENDS = "libmnl libtool"

SRC_URI = "https://ipset.netfilter.org/${BP}.tar.bz2"

SRC_URI[md5sum] = "7c17aca72bcf852f5bc95582aaa60408"
SRC_URI[sha256sum] = "3151baad30f1d9e317b2ab4f2f5aa7a9f7b4dc11fcf8fe73acd0dc0b5dbabf7d"

inherit autotools pkgconfig

# SSAM needs access to the libipset.so shared library (fixme: is that true?)
# Even when the shared lib is built, the ipset binary will default to linking
# statically unless the static lib is explicitly disabled.

EXTRA_OECONF += " \
    --with-kmod=no \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ssam', '--disable-static', '--disable-shared', d)} \
"
