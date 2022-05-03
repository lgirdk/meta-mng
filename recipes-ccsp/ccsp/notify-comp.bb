SUMMARY = "CCSP Notify Component"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8da35c40378155af4c5404b8f72d1237"

require ccsp_common.inc

DEPENDS += "utopia"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git/notify_comp"

inherit autotools pkgconfig

DATAMODEL_XML = "scripts/NotifyComponent.xml"

do_install_append () {
	install -d ${D}/usr/ccsp/notify-comp
	install -m 644 ${S}/scripts/msg_daemon.cfg ${D}/usr/ccsp/notify-comp/
}

FILES_${PN} += "/usr/ccsp"
