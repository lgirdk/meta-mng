SUMMARY = "Comcast specific wrapper for libcap"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "libcap jsoncpp"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

do_install_append () {
	# Install the RDKB specific version of process-capabilities.json
	install -d ${D}${sysconfdir}/security/caps
	install -m 644 ${S}/source/process-capabilities_rdkb.json ${D}${sysconfdir}/security/caps/process-capabilities.json
}
