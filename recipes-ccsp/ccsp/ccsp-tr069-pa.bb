SUMMARY = "CCSP Tr069 Protocol Adaptor"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia halinterface telemetry cjson openssl util-linux libunpriv"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${CCSP_CONFIG_ARCH}"

CFLAGS += " \
    -I${STAGING_INCDIR}/syscfg \
"

LDFLAGS += " \
    -ltelemetry_msgsender \
"

do_install_append () {
	install -d ${D}/usr/ccsp/tr069pa
	install -m 644 ${S}/config/ccsp_tr069_pa_certificate_cfg_arm.xml  ${D}/usr/ccsp/tr069pa/ccsp_tr069_pa_certificate_cfg.xml
	install -m 644 ${S}/config/ccsp_tr069_pa_cfg_arm.xml              ${D}/usr/ccsp/tr069pa/ccsp_tr069_pa_cfg.xml
	install -m 644 ${S}/config/ccsp_tr069_pa_mapper_arm.xml           ${D}/usr/ccsp/tr069pa/ccsp_tr069_pa_mapper.xml
	install -m 644 ${S}/config/sdm_arm.xml                            ${D}/usr/ccsp/tr069pa/sdm.xml
	install -m 644 ${S}/arch/intel_usg/config/url                     ${D}/usr/ccsp/tr069pa/url

	echo "5555" > ${D}/usr/ccsp/tr069pa/sharedkey
}

FILES_${PN} += "/usr/ccsp"

RPROVIDES_${PN} += "${PN}-ccsp"
