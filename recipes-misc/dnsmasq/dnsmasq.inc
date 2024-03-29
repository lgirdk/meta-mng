SUMMARY = "Lightweight, easy to configure DNS forwarder and DHCP server"
HOMEPAGE = "http://www.thekelleys.org.uk/dnsmasq/doc.html"
SECTION = "net"
# GPLv3 was added in version 2.41 as license option
LICENSE = "GPLv2 | GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                    file://COPYING-v3;md5=d32239bcb673463ab874e80d47fae504"

#at least versions 2.69 and prior are moved to the archive folder on the server
SRC_URI = "http://www.thekelleys.org.uk/dnsmasq/${@['archive/', ''][float(d.getVar('PV', True).split('.')[1]) > 69]}dnsmasq-${PV}.tar.xz \
           file://init \
           file://dnsmasq.conf \
           file://dnsmasq-resolvconf.service \
           file://dnsmasq-noresolvconf.service \
           file://dnsmasq-resolved.conf \
           file://dnsmasq.resolvconf \
           file://dnsmasq-resolvconf-helper \
           file://99_dnsmasq \
"

inherit pkgconfig update-rc.d systemd useradd

PACKAGECONFIG ?= "rtc"

PACKAGECONFIG[dbus] = ",,dbus"
PACKAGECONFIG[idn] = ",,libidn"
PACKAGECONFIG[libidn2] = ",,libidn2"
PACKAGECONFIG[conntrack] = ",,libnetfilter-conntrack"
PACKAGECONFIG[lua] = ",,lua"
PACKAGECONFIG[resolvconf] = ",,,resolvconf"
PACKAGECONFIG[dnssec] = ",,nettle gmp"
PACKAGECONFIG[rtc] = ",,"

ASNEEDED = ""

LDFLAGS_append_libc-glibc = " -lmcheck"

EXTRA_OEMAKE = "\
    'COPTS=${@bb.utils.contains('PACKAGECONFIG', 'dbus', '-DHAVE_DBUS', '', d)} \
           ${@bb.utils.contains('PACKAGECONFIG', 'idn', '-DHAVE_IDN', '', d)} \
           ${@bb.utils.contains('PACKAGECONFIG', 'libidn2', '-DHAVE_LIBIDN2', '', d)} \
           ${@bb.utils.contains('PACKAGECONFIG', 'conntrack', '-DHAVE_CONNTRACK', '', d)} \
           ${@bb.utils.contains('PACKAGECONFIG', 'lua', '-DHAVE_LUASCRIPT', '', d)} \
           ${@bb.utils.contains('PACKAGECONFIG', 'dnssec', '-DHAVE_DNSSEC', '', d)} \
           ${@bb.utils.contains('PACKAGECONFIG', 'rtc', '', '-DHAVE_BROKEN_RTC', d)} \
    ' \
    'CFLAGS=${CFLAGS}' \
    'LDFLAGS=${LDFLAGS}' \
    'PREFIX=${prefix}' \
"

# Debian etc install dnsmasq to /sbin but meta-oe has historically installed to /usr/bin
# Keep the historical meta-oe behaviour for now, even though it's not very standard.
# EXTRA_OEMAKE += "'BINDIR=${base_sbindir}'"
EXTRA_OEMAKE += "'BINDIR=${bindir}'"

do_compile () {
    oe_runmake
    oe_runmake -C contrib/lease-tools
}

do_install () {
    oe_runmake 'DESTDIR=${D}' install

    install -d ${D}${bindir}
    install -m 0755 ${B}/contrib/lease-tools/dhcp_release ${D}${bindir}/

    install -d ${D}${sysconfdir}/ ${D}${sysconfdir}/init.d ${D}${sysconfdir}/dnsmasq.d
    install -m 644 ${WORKDIR}/dnsmasq.conf ${D}${sysconfdir}/
    install -m 755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/dnsmasq

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}
    then
        install -d ${D}${systemd_unitdir}/system
        if ${@bb.utils.contains('PACKAGECONFIG','resolvconf','true','false',d)}
        then
            install -m 0644 ${WORKDIR}/dnsmasq-resolvconf.service ${D}${systemd_unitdir}/system/dnsmasq.service
        else
            install -m 0644 ${WORKDIR}/dnsmasq-noresolvconf.service ${D}${systemd_unitdir}/system/dnsmasq.service
        fi
        install -d ${D}${sysconfdir}/systemd/resolved.conf.d/
        install -m 0644 ${WORKDIR}/dnsmasq-resolved.conf ${D}${sysconfdir}/systemd/resolved.conf.d/
    fi

    if ${@bb.utils.contains('PACKAGECONFIG','dbus','true','false',d)}
    then
        install -d ${D}${sysconfdir}/dbus-1/system.d
        install -m 644 dbus/dnsmasq.conf ${D}${sysconfdir}/dbus-1/system.d/
    fi

    if ${@bb.utils.contains('PACKAGECONFIG','resolvconf','true','false',d)}
    then
        install -d ${D}${sysconfdir}/resolvconf/update.d
        install -m 0755 ${WORKDIR}/dnsmasq.resolvconf ${D}${sysconfdir}/resolvconf/update.d/dnsmasq

        install -d ${D}${sysconfdir}/default/volatiles
        install -m 0644 ${WORKDIR}/99_dnsmasq ${D}${sysconfdir}/default/volatiles
        install -m 0755 ${WORKDIR}/dnsmasq-resolvconf-helper ${D}${bindir}
    fi
}

INITSCRIPT_NAME = "dnsmasq"
INITSCRIPT_PARAMS = "defaults"

CONFFILES_${PN} = "${sysconfdir}/dnsmasq.conf"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --home /nonexistent -M --shell /bin/false --no-user-group --gid nogroup dnsmasq"

RPROVIDES_${PN} += "${PN}-systemd"
RREPLACES_${PN} += "${PN}-systemd"
RCONFLICTS_${PN} += "${PN}-systemd"
SYSTEMD_SERVICE_${PN} = "dnsmasq.service"
