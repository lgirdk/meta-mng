SUMMARY = "CCSP MoCA"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia hal-moca curl msgpack-c trower-base64"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${CCSP_CONFIG_ARCH} ${CCSP_CONFIG_PLATFORM}"

CFLAGS += " \
    -I${STAGING_INCDIR}/syscfg \
    -I${STAGING_INCDIR}/ulog \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
    -I${STAGING_INCDIR}/msgpackc \
    -I${STAGING_INCDIR}/trower-base64 \
"

CFLAGS += " \
    -DCONFIG_CISCO_HOTSPOT \
    -DCONFIG_VENDOR_CUSTOMER_COMCAST \
"

LDFLAGS += " \
    -lutctx \
    -lutapi \
    -lmsgpackc \
    -ltrower-base64 \
"

do_compile_prepend () {
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/TR181-MoCA.XML ${S}/source/MoCASsp/dm_pack_datamodel.c )
}

do_install_append () {
	install -d ${D}/usr/ccsp/moca
	install -m 644 ${S}/config/CcspMoCA.cfg ${D}/usr/ccsp/moca/
	install -m 644 ${S}/config/CcspMoCADM.cfg ${D}/usr/ccsp/moca/
	install -m 755 ${S}/scripts/MoCA_isolation.sh ${D}/usr/ccsp/moca/
}

FILES_${PN} += "/usr/ccsp"

RPROVIDES_${PN} += "${PN}-ccsp"
