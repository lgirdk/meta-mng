SUMMARY = "CCSP Hotspot"
LICENSE = "Apache-2.0 & ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7fd38647ff87fdac48b3fb87e20c1b07"

require ccsp_common.inc

DEPENDS += "utopia telemetry ccsp-lm-lite libnetfilter-queue"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/hotspot${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig siteinfo

# Workaround for dhcpsnooper.c which relies on #ifdef __686__ to detect little endian targets
CFLAGS += "${@oe.utils.conditional('SITEINFO_ENDIANNESS', 'le', '-D__686__', '', d)}"

DATAMODEL_XML = "source/hotspotfd/config/hotspot.XML"

do_install_append () {
	install -d ${D}${includedir}/ccsp
	install -m 644 ${S}/source/hotspotfd/include/dhcpsnooper.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/hotspotfd/include/hotspotfd.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/HotspotApi/libHotspotApi.h ${D}${includedir}/ccsp/

	install -d ${D}/usr/ccsp/hotspot
	ln -sf ${bindir}/CcspHotspot ${D}/usr/ccsp/hotspot/CcspHotspot
}

FILES_${PN} += "/usr/ccsp"
