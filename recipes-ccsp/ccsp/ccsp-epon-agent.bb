SUMMARY = "CCSP EPON Agent"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "hal-epon hal-cm hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi utopia"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

LDFLAGS += "-lsyscfg"

do_compile_prepend () {
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/TR181-EPON.XML ${S}/source/EPONAgentSsp/dm_pack_datamodel.c )
}

do_install_append () {
	install -d ${D}/usr/ccsp/epon
	install -m 644 ${S}/config/CcspEPONDM.cfg ${D}/usr/ccsp/epon/CcspEPONDM.cfg
	install -m 644 ${S}/config/CcspEPON.cfg ${D}/usr/ccsp/epon/CcspEPON.cfg

	ln -sf ${bindir}/CcspEPONAgentSsp ${D}/usr/ccsp/epon/CcspEPONAgentSsp
}

FILES_${PN} += "/usr/ccsp"
