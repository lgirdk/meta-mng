SUMMARY = "CCSP Common Library - Host scripts etc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=19774cd4dd519f099bc404798ceeab19"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit native

do_configure () {
	:
}

do_compile () {
	:
}

do_install () {
	install -d ${D}${bindir}
	install -m 644 ${S}/source/dm_pack/dm_pack_code_gen.py ${D}${bindir}/
}
