SUMMARY = "CCSP Component Registry"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia telemetry libunpriv libxml2"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES','systemd','--enable-notify','',d)}"

CFLAGS += " \
    -I${STAGING_INCDIR}/syscfg \
    -I${STAGING_INCDIR}/ulog \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
    -I${STAGING_INCDIR}/libxml2 \
"

LDFLAGS += " \
    -ltelemetry_msgsender \
    -lprivilege \
    -lutapi \
    -lutctx \
    -lsyscfg \
"

do_install_append () {
	install -d ${D}/usr/ccsp
	install -m 644 ${S}/source/cr-ethwan-deviceprofile.xml ${D}/usr/ccsp/
	install -m 644 ${S}/config/cr-deviceprofile_embedded.xml ${D}/usr/ccsp/cr-deviceprofile.xml

	ln -sf ${bindir}/CcspCrSsp ${D}/usr/ccsp/CcspCrSsp
}

FILES_${PN} += "/usr/ccsp"
