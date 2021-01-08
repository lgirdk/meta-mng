SUMMARY = "CCSP LM Lite"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

require ccsp_common.inc

DEPENDS += "utopia hal-ethsw hal-moca telemetry avro-c cimplog curl libparodus libxml2 msgpack-c nanomsg trower-base64 util-linux wrp-c"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${CCSP_CONFIG_ARCH} ${CCSP_CONFIG_PLATFORM}"

CFLAGS += " \
    -I${STAGING_INCDIR}/cimplog \
    -I${STAGING_INCDIR}/libparodus \
    -I${STAGING_INCDIR}/libxml2 \
    -I${STAGING_INCDIR}/trower-base64 \
"

LDFLAGS += " \
    -ltelemetry_msgsender \
    -lcurl \
    -lxml2 \
"

do_compile_prepend () {
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/LMLite.XML ${S}/source/Ssp/dm_pack_datamodel.c )
}

#force lib to be built first
do_compile () {
	oe_runmake liblmapi.la
	oe_runmake all
}

do_install_append () {
	install -d ${D}/usr/ccsp/lm
	install -m 644 ${S}/config/NetworkDevicesStatus.avsc ${D}/usr/ccsp/lm/
	install -m 644 ${S}/config/NetworkDevicesTraffic.avsc ${D}/usr/ccsp/lm/

	install -d ${D}/${includedir}
	install -m 644 ${S}/source/lm/lm_api.h ${D}${includedir}/

	ln -sf ${bindir}/CcspLMLite ${D}/usr/ccsp/lm/CcspLMLite
}

FILES_${PN} += "/usr/ccsp"

RPROVIDES_${PN} += "${PN}-ccsp"
