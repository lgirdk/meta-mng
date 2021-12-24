SUMMARY = "CCSP Data Model Command Line Interface"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "telemetry utopia"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

do_compile_prepend () {
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/source/MsgBusTestServer/config/MsgBusTest.XML ${S}/source/MsgBusTestServer/dm_pack_datamodel.c )
}

do_install_append () {
	install -d ${D}/usr/ccsp/MsgBusTestServer
}

FILES_${PN} += "/usr/ccsp"
