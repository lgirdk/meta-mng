SUMMARY = "CCSP Misc Tools"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "hal-platform trower-base64 rdklist utopia"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += " \
    ${@bb.utils.contains('DISTRO_FEATURES','multipartUtility','--enable-multipartUtilEnable=yes','',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES','notifylease','--enable-notifylease','',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES','wbCfgTestApp','--enable-wbCfgTestAppEnable','',d)} \
"

CFLAGS += " \
    -DDHCPV6_CLIENT_DIBBLER \
    -I${STAGING_INCDIR}/syscfg \
    -I${STAGING_INCDIR}/trower-base64 \
"

do_install_append () {
	install -d ${D}${includedir}/ccsp
	install -m 644 ${S}/source/dhcp_client_utils/dhcp_client_utils.h ${D}${includedir}/ccsp/
	install -d ${D}/usr/ccsp
	ln -sf ${bindir}/psmcli ${D}/usr/ccsp/psmcli
}

FILES_${PN} += "/usr/ccsp"
