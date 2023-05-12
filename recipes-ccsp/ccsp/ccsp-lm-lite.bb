SUMMARY = "CCSP LM Lite"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

require ccsp_common.inc

DEPENDS += "utopia hal-ethsw telemetry avro-c curl libparodus libsyswrapper libunpriv libxml2 msgpack-c nanomsg trower-base64 util-linux wrp-c"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'moca', 'hal-moca', '', d)}"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

CFLAGS += " \
    -I${STAGING_INCDIR}/libxml2 \
"

CFLAGS += " \
    -DDEVICE_GATEWAY_ASSOCIATION_FEATURE \
"
DATAMODEL_XML = "config/LMLite.XML"

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
