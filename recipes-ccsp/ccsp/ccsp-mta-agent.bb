SUMMARY = "CCSP MTA Agent"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia hal-mta cjson"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

LDFLAGS += "-lcjson"

do_compile_prepend () {
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/CcspMtaAgent.xml ${S}/source/MtaAgentSsp/dm_pack_datamodel.c )
}

do_install_append () {
	install -d ${D}/usr/ccsp/mta
	install -m 644 ${S}/config/CcspMta.cfg ${D}/usr/ccsp/mta/
	install -m 644 ${S}/config/CcspMtaLib.cfg ${D}/usr/ccsp/mta/

	if ${@bb.utils.contains('DISTRO_FEATURES','tdkb','true','false',d)}
	then
		install -d ${D}${includedir}/ccsp
		install -m 644 ${S}/source/TR-181/include/*.h ${D}${includedir}/ccsp/

		install -d ${D}${includedir}/middle_layer_src/mta
		install -m 644 ${S}/source/TR-181/middle_layer_src/*.h ${D}${includedir}/middle_layer_src/mta/
	fi
}

FILES_${PN} += "/usr/ccsp"
