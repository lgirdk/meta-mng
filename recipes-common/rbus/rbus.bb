SUMMARY = "RBus high level library and test utils, etc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ed63516ecab9f06e324238dd2b259549"

DEPENDS = "rbus-core rtmessage linenoise"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

do_install_append () {
	# Fix include paths within rbus headers to match path in sysroot
	for f in ${D}${includedir}/rbus/*.h ; do
		sed -i 's|^#include <rbus|#include <rbus/rbus|' $f
	done
}

FILES_${PN}-dev += "${libdir}/cmake"
