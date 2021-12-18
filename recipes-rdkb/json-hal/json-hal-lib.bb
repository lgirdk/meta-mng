SUMMARY = "JSON HAL Client and Server library"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=279c0d21cb7bc051383cef7cd415c938"

DEPENDS = "json-c json-schema-validator"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/json-rpc${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

# Note: Source file modifications should be done as part of do_patch (rather
# than as part of do_configure) to avoid potential problems with sstate cache.

tweak_warning_options() {
	if [ "${TOOLCHAIN}" = "clang" ]; then
		sed 's/-Wno-error=discarded-qualifiers/-Wno-error=incompatible-pointer-types-discards-qualifiers/' -i ${S}/CMakeLists.txt
	fi
}

do_patch_append() {
    # This is python, not shell, so indent with 4 spaces
    bb.build.exec_func('tweak_warning_options', d)
}
