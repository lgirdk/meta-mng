SUMMARY = "CCSP Cable Modem Agent"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia hal-cm hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' systemd', '', d)}"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES','systemd','--enable-notify','',d)}"

do_compile_prepend () {
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config-arm/TR181-CM.XML ${S}/source/CMAgentSsp/dm_pack_datamodel.c )
}

do_install_append () {
	install -d ${D}/usr/ccsp/cm
	ln -sf ${bindir}/CcspCMAgentSsp ${D}/usr/ccsp/cm/CcspCMAgentSsp

	install -m 644 ${S}/config-arm/CcspCMDM.cfg ${D}/usr/ccsp/cm/CcspCMDM.cfg
	install -m 644 ${S}/config-arm/CcspCM.cfg ${D}/usr/ccsp/cm/CcspCM.cfg

	install -d ${D}${includedir}/ccsp
	install -m 644 ${S}/source/TR-181/include/*.h ${D}${includedir}/ccsp/

	install -d ${D}${includedir}/middle_layer_src/cm
	install -m 644 ${S}/source/TR-181/middle_layer_src/*.h ${D}${includedir}/middle_layer_src/cm/
}

FILES_${PN} += "/usr/ccsp"

RPROVIDES_${PN} += "${PN}-ccsp"
