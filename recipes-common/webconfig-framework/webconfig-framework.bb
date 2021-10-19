SUMMARY = "Webconfig Framework"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bc21fa26f9718980827123b8b80c0ded"

require recipes-ccsp/ccsp/ccsp_common.inc

DEPENDS += "rbus rbus-core"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "--enable-ccspsupport"

CFLAGS += "-DCCSP_SUPPORT_ENABLED -DWBCFG_MULTI_COMP_SUPPORT"

do_install_append () {
	install -d ${D}${includedir}
	install -m 644 ${S}/include/*.h ${D}${includedir}/
}
