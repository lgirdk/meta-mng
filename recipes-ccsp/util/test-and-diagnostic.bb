SUMMARY = "CCSP Test and Diagnostic Utilities"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=06093b681f6d882a55e3bc222a02a988"

require recipes-ccsp/ccsp/ccsp_common.inc

DEPENDS += "utopia hal-mta hal-platform rbus libev libpcap"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES','core-net-lib','core-net-lib','',d)}"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "--enable-mta"
EXTRA_OECONF += "--enable-core_net_lib_feature_support=${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', 'yes', 'no', d)}"

CFLAGS += " \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
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
