SUMMARY = "CCSP Component Registry"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia telemetry libunpriv"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES','systemd','--enable-notify','',d)}"

CFLAGS += " \
    -I${STAGING_INCDIR}/syscfg \
    -I${STAGING_INCDIR}/ulog \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
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

RPROVIDES_${PN} += "${PN}-ccsp"
