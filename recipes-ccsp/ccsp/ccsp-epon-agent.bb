SUMMARY = "CCSP EPON Agent"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia hal-epon hal-platform"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

LDFLAGS += "-lsyscfg"

DATAMODEL_XML = "config/TR181-EPON.XML"

do_install_append () {
	install -d ${D}/usr/ccsp/epon
	install -m 644 ${S}/config/CcspEPONDM.cfg ${D}/usr/ccsp/epon/CcspEPONDM.cfg
	install -m 644 ${S}/config/CcspEPON.cfg ${D}/usr/ccsp/epon/CcspEPON.cfg

	ln -sf ${bindir}/CcspEPONAgentSsp ${D}/usr/ccsp/epon/CcspEPONAgentSsp
}

FILES_${PN} += "/usr/ccsp"
