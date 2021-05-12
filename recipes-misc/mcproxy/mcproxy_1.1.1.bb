SUMMARY = "Mcproxy is an IGMP/MLD Proxy daemon for Linux"
HOMEPAGE = "https://mcproxy.realmv6.org/trac"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PV .= "+git${SRCPV}"

SRCREV = "93b5ace42268160ebbfff4c61818fb15fa2d9b99"

SRC_URI = "git://github.com/mcproxy/mcproxy;protocol=https \
           file://0001-add-cmake.patch \
           file://0002-add-local-copy-of-getsourcefilter-setsourcefilter.patch \
           file://0003-fix-mutex_lock-on-a-non-mutable-member.patch \
           file://0004-fix-deleting-while-iterating-through-set-STL-in-C.patch \
"

S = "${WORKDIR}/git"

inherit cmake

do_install_append() {
	# Create alias symlinks for v4 and v6, to run as separate instances
	ln -sf mcproxy-bin ${D}${bindir}/mcproxy_v4
	ln -sf mcproxy-bin ${D}${bindir}/mcproxy_v6
}
