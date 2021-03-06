SUMMARY = "Administration tool for IP sets"
HOMEPAGE = "http://ipset.netfilter.org"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

DEPENDS = "libmnl libtool"

SRC_URI = "http://ipset.netfilter.org/${BP}.tar.bz2"

SRC_URI[md5sum] = "e107b679c3256af795261cece864d6d9"
SRC_URI[sha256sum] = "0e7d44caa9c153d96a9b5f12644fbe35a632537a5a7f653792b72e53d9d5c2db"

inherit autotools pkgconfig

EXTRA_OECONF += "--with-kmod=no --disable-shared"
