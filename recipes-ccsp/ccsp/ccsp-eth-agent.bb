SUMMARY = "CCSP Ethernet Agent"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia hal-ethsw hal-platform ccsp-lm-lite cimplog curl libunpriv"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

PACKAGECONFIG ?= "dropearly"

PACKAGECONFIG[dropearly] = "--enable-dropearly,--disable-dropearly"

CFLAGS += " \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
"

DATAMODEL_XML = "config/TR181-EthAgent.xml"

do_install_append () {
	install -d ${D}/usr/ccsp/ethagent
}

FILES_${PN} += "/usr/ccsp"
