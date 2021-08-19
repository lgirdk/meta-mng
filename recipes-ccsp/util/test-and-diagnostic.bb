SUMMARY = "CCSP Test and Diagnostic Utilities"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require recipes-ccsp/ccsp/ccsp_common.inc

DEPENDS += "utopia hal-mta hal-platform"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "--enable-mta"

CFLAGS += " \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
    -I${STAGING_INCDIR}/ulog \
    -I${STAGING_INCDIR}/syscfg \
"

do_compile_prepend () {
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/TestAndDiagnostic_arm.XML ${S}/source/TandDSsp/dm_pack_datamodel.c )
}

do_install_append () {
	install -d ${D}${includedir}/ccsp
	install -m 644 ${S}/source/dmltad/diag*.h ${D}${includedir}/ccsp/

	install -d ${D}/usr/ccsp/tad
	install -m 755 ${S}/scripts/*.sh ${D}/usr/ccsp/tad/
	install -m 755 ${S}/source/CpuMemFrag/cpumemfrag_cron.sh ${D}/usr/ccsp/tad/
	install -m 755 ${S}/source/CpuMemFrag/log_buddyinfo.sh ${D}/usr/ccsp/tad/

	install -d ${D}${sbindir}
	install -m 755 ${S}/scripts/arping_peer ${D}${sbindir}/
	install -m 755 ${S}/scripts/ping_peer ${D}${sbindir}/

	ln -sf /usr/bin/CcspTandDSsp ${D}/usr/ccsp/tad/CcspTandDSsp
}

FILES_${PN} += "/usr/ccsp"
