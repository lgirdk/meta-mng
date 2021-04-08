SUMMARY = "RDKB Telemetry"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require recipes-ccsp/ccsp/ccsp_common.inc

DEPENDS += "cjson curl rdklist libunpriv rbus utopia"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "--enable-ccspsupport"

CFLAGS += " \
    -DCCSP_SUPPORT_ENABLED \
    -DENABLE_RDKB_SUPPORT \
    -DFEATURE_SUPPORT_WEBCONFIG \
"

do_compile_prepend () {
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/TR181-T2-USGv2.XML ${S}/source/t2ssp/dm_pack_datamodel.c )
}

do_install_append () {

	install -d ${D}${includedir}
	install -m 644 ${S}/include/telemetry2_0.h ${D}${includedir}/
	install -m 644 ${S}/include/telemetry_busmessage_sender.h ${D}${includedir}/

	install -d ${D}/usr/ccsp/telemetry
	install -m 644 ${S}/config/CcspDmLib.cfg ${D}/usr/ccsp/telemetry/
	install -m 644 ${S}/config/T2Agent.cfg ${D}/usr/ccsp/telemetry/
	install -m 644 ${S}/source/bulkdata/conf/DCMresponse.txt ${D}/usr/ccsp/telemetry/

	install -d ${D}/lib/rdk
	install -m 755 ${S}/source/commonlib/t2Shared_api.sh ${D}/lib/rdk
	install -m 755 ${S}/source/interChipHelper/scripts/interChipUtils.sh ${D}/lib/rdk/
	install -m 755 ${S}/source/bulkdata/scripts/autodownload_dcmconfig.sh ${D}/lib/rdk/
}

FILES_${PN} += "/usr/ccsp /lib/rdk"
