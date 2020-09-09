SUMMARY = "CCSP Misc Tools"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "hal-platform trower-base64"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${CCSP_CONFIG_ARCH}"

EXTRA_OECONF += " \
    ${@bb.utils.contains('DISTRO_FEATURES','multipartUtility','--enable-multipartUtilEnable=yes','',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES','notifylease','--enable-notifylease','',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES','wbCfgTestApp','--enable-wbCfgTestAppEnable','',d)} \
"

do_install_append () {
	install -d ${D}/usr/ccsp
	ln -sf ${bindir}/psmcli ${D}/usr/ccsp/psmcli
}

FILES_${PN} += "/usr/ccsp"

RPROVIDES_${PN} += "${PN}-ccsp"
