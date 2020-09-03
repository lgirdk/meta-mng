SUMMARY = "CCSP Ethernet Agent"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia hal-ethsw hal-platform curl ccsp-lm-lite cimplog libunpriv"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

PACKAGECONFIG ?= "dropearly"

PACKAGECONFIG[dropearly] = "--enable-dropearly,--disable-dropearly"

EXTRA_OECONF += "${CCSP_CONFIG_ARCH} ${CCSP_CONFIG_PLATFORM}"

CFLAGS += " \
    -I${STAGING_INCDIR}/cimplog \
    -I${STAGING_INCDIR}/syscfg \
    -I${STAGING_INCDIR}/ulog \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
"

LDFLAGS += " \
    -lccsp_common \
    -lutctx \
    -lutapi \
    -lcimplog \
    -lprivilege \
"

do_compile_prepend () {
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/TR181-EthAgent.xml ${S}/source/EthSsp/dm_pack_datamodel.c )
}

do_install_append () {
	install -d ${D}/usr/ccsp/ethagent
}

FILES_${PN} += "/usr/ccsp"
