SUMMARY = "CCSP Hotspot"
LICENSE = "Apache-2.0 & ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7fd38647ff87fdac48b3fb87e20c1b07"

require ccsp_common.inc

DEPENDS += "utopia telemetry ccsp-lm-lite libnetfilter-queue"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/hotspot.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig siteinfo

EXTRA_OECONF += "${CCSP_CONFIG_ARCH} ${CCSP_CONFIG_PLATFORM}"

# Workaround for dhcpsnooper.c which relies on #ifdef __686__ to detect little endian targets
CFLAGS += "${@oe.utils.conditional('SITEINFO_ENDIANNESS', 'le', '-D__686__', '', d)}"

LDFLAGS += "-ltelemetry_msgsender"

do_compile_prepend(){
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/source/hotspotfd/config/hotspot.XML ${S}/source/hotspotfd/dm_pack_datamodel.c )
}

do_install_append () {
	install -d ${D}${includedir}/ccsp
	install -m 644 ${S}/source/hotspotfd/include/dhcpsnooper.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/hotspotfd/include/hotspotfd.h ${D}${includedir}/ccsp/

	# These were previously installed by a .bbappend in meta-cmf-broadband. Fixme: still required ?
	# install -m 644 ${S}/source/hotspotfd/include/debug.h ${D}${includedir}/ccsp/
	# install -m 644 ${S}/source/hotspotfd/include/dhcp.h ${D}${includedir}/ccsp/

	install -d ${D}/usr/ccsp/hotspot
	ln -sf ${bindir}/CcspHotspot ${D}/usr/ccsp/hotspot/CcspHotspot
}

FILES_${PN} += "/usr/ccsp"

RPROVIDES_${PN} += "${PN}-ccsp"
