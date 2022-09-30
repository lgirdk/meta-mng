SUMMARY = "CCSP Tr069 Protocol Adaptor"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia halinterface telemetry cjson openssl util-linux libunpriv"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

do_install_append () {
	install -d ${D}/usr/ccsp/tr069pa
	install -m 644 ${S}/config/ccsp_tr069_pa_certificate_cfg_arm.xml  ${D}/usr/ccsp/tr069pa/ccsp_tr069_pa_certificate_cfg.xml
	install -m 644 ${S}/config/ccsp_tr069_pa_cfg_arm.xml              ${D}/usr/ccsp/tr069pa/ccsp_tr069_pa_cfg.xml
	install -m 644 ${S}/config/ccsp_tr069_pa_mapper_arm.xml           ${D}/usr/ccsp/tr069pa/ccsp_tr069_pa_mapper.xml
	install -m 644 ${S}/config/sdm_arm.xml                            ${D}/usr/ccsp/tr069pa/sdm.xml
}

FILES_${PN} += "/usr/ccsp"

RDEPENDS_${PN} += "ccsp-tr069-cacert"
