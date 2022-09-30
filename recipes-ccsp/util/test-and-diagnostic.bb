SUMMARY = "CCSP Test and Diagnostic Utilities"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9c4d64ad248641f8dd76f69edff1c27a"

require recipes-ccsp/ccsp/ccsp_common.inc

DEPENDS += "utopia hal-mta hal-platform rbus libev"

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

DATAMODEL_XML = "config/TestAndDiagnostic_arm.XML"

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

# scripts/resource_monitor.sh contains calls to slabtop
RDEPENDS_${PN} += "procps"
