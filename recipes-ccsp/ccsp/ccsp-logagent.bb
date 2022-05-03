SUMMARY = "CCSP Log Agent"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES','systemd','systemd','',d)}"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES','systemd','--enable-notify','',d)}"

DATAMODEL_XML = "scripts/LogAgent.xml"

do_install_append () {
	install -d ${D}/usr/ccsp/logagent
	install -m 644 ${S}/scripts/msg_daemon.cfg ${D}/usr/ccsp/logagent/msg_daemon.cfg
}

FILES_${PN} += "/usr/ccsp"
