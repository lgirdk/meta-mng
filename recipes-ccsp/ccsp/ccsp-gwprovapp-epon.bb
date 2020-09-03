SUMMARY = "CCSP GWProvAPP EPON"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "hal-cm hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi ruli utopia"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/gwprovapp-epon.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig systemd

CFLAGS += " \
    ${@bb.utils.contains('DISTRO_FEATURES','bci','-DCISCO_CONFIG_TRUE_STATIC_IP -DCISCO_CONFIG_DHCPV6_PREFIX_DELEGATION','',d)} \
"

do_install_append () {
	install -d ${D}/usr/ccsp
	install -m 755 ${B}/source/gw_prov_epon ${D}/usr/ccsp/

	install -d ${D}${systemd_unitdir}/system
	install -m 644 ${S}/service/gwprovepon.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_SERVICE_${PN} += "gwprovepon.service"

FILES_${PN} += "/usr/ccsp"
