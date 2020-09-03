SUMMARY = "C wrapper for Google Breakpad"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "breakpad"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

CPPFLAGS += "-I${STAGING_INCDIR}/breakpad"

LDFLAGS += "-lbreakpad_client"

do_install_append () {
    install -d ${D}${includedir}
    install -m 0644 ${S}/*.h ${D}${includedir}/
}

FILES_${PN} += "${libdir}/*.so"
