SUMMARY = "Dibbler DHCPv6 client"
DESCRIPTION = "Dibbler is a portable DHCPv6 implementation. It supports stateful as well as stateless autoconfiguration for IPv6."
HOMEPAGE = "http://klub.com.pl/dhcpv6"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7236695bb6d4461c105d685a8b61c4e3"

DEPENDS = "flex-native"

PV = "1.0.1+1.0.2RC1+git${SRCPV}"

SRCREV = "a7c6cf58a88a510cb00841351e75030ce78d36bf"

SRC_URI = "git://github.com/tomaszmrugalski/dibbler \
           file://dibbler_fix_getSize_crash.patch \
           file://dibbler_crash_fix.patch \
           file://avoid-crash-in-delete_radvd_conf.patch \
           file://fix-misguided-and-broken-usage-of-clock_gettime-CLOC.patch \
           file://add-client-support-for-SOL_MAX_RT-option.patch \
"

S = "${WORKDIR}/git"

inherit autotools

PACKAGECONFIG ??= "bind-reuse debug dns-update"

PACKAGECONFIG[auth] = "--enable-auth,,,"
PACKAGECONFIG[bind-reuse] = "--enable-bind-reuse,,,"
PACKAGECONFIG[debug] = "--enable-debug,,,"
PACKAGECONFIG[dns-update] = "--enable-dns-update,,,"
PACKAGECONFIG[dst-addr-filter] = "--enable-dst-addr-check,,,"
PACKAGECONFIG[efence] = "--enable-efence,,,"
PACKAGECONFIG[gtest] = "--enable-gtest-static,,,"
PACKAGECONFIG[resolvconf] = "--enable-resolvconf,,,"

PACKAGES =+ "${PN}-client ${PN}-relay ${PN}-requestor ${PN}-server"

FILES_${PN}-client = "${sbindir}/${PN}-client"
FILES_${PN}-relay = "${sbindir}/${PN}-relay"
FILES_${PN}-requestor = "${sbindir}/${PN}-requestor"
FILES_${PN}-server = "${sbindir}/${PN}-server"

RDEPENDS_${PN}-client += "${@bb.utils.contains('PACKAGECONFIG', 'resolvconf', 'resolvconf', '', d)}"
