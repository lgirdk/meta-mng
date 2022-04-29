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

do_compile_prepend () {
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/${DATAMODEL_XML} ${S}/source/NotifyComponent/dm_pack_datamodel.c )
}

do_install_append () {
	install -d ${D}/usr/ccsp/notify-comp
	install -m 644 ${S}/scripts/msg_daemon.cfg ${D}/usr/ccsp/notify-comp/
}

FILES_${PN} += "/usr/ccsp"
