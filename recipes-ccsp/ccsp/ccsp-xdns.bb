SUMMARY = "CCSP XDNS"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

require ccsp_common.inc

DEPENDS += "utopia trower-base64"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${CCSP_CONFIG_ARCH}"

CFLAGS += " \
    -I${STAGING_INCDIR}/ulog \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
    -I${STAGING_INCDIR}/trower-base64 \
"

LDFLAGS += " \
    -lutctx \
    -lutapi \
"

DATAMODEL_XML = "config/CcspXdns_dm.xml"

do_install_append () {
	install -d ${D}/usr/ccsp/xdns
}

FILES_${PN} += "/usr/ccsp"
