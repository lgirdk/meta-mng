SUMMARY = "Administration tool for IP sets"
HOMEPAGE = "https://ipset.netfilter.org"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

DEPENDS = "libmnl libtool"

SRC_URI = "https://ipset.netfilter.org/${BP}.tar.bz2"

SRC_URI[md5sum] = "b681a86dbdb2d9726245af739bca01ac"
SRC_URI[sha256sum] = "0a5545aaadb640142c1f888d366a78ddf8724799967fa20686a70053bd621751"

inherit autotools pkgconfig

# SSAM needs access to the libipset.so shared library (fixme: is that true?)
# Even when the shared lib is built, the ipset binary will default to linking
# statically unless the static lib is explicitly disabled.

EXTRA_OECONF += " \
    --with-kmod=no \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ssam', '--disable-static', '--disable-shared', d)} \
"
