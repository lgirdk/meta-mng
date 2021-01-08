SUMMARY = "CCSP GWProvAPP ETHWAN"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia telemetry"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/gwprovapp-ethwan${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig systemd

CFLAGS += " \
    ${@bb.utils.contains('DISTRO_FEATURES','bci','-DCISCO_CONFIG_TRUE_STATIC_IP -DCISCO_CONFIG_DHCPV6_PREFIX_DELEGATION','',d)} \
"

do_install_append () {
	install -d ${D}/usr/ccsp
	install -m 755 ${B}/source/gw_prov_ethwan ${D}/usr/ccsp/

	install -d ${D}${systemd_unitdir}/system
	install -m 644 ${S}/service/gwprovethwan.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_SERVICE_${PN} += "gwprovethwan.service"

FILES_${PN} += "/usr/ccsp"
