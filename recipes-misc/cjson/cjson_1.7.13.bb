DESCRIPTION = "Ultralightweight JSON parser in ANSI C"
AUTHOR = "Dave Gamble"
HOMEPAGE = "https://github.com/DaveGamble/cJSON"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=218947f77e8cb8e2fa02918dc41c50d0"

SRC_URI = "git://github.com/DaveGamble/cJSON.git"
SRCREV = "39853e5148dad8dc5d32ea2b00943cf4a0c6f120"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE += "\
    -DENABLE_CJSON_TEST=OFF \
    -DENABLE_CJSON_UTILS=OFF \
    -DENABLE_CUSTOM_COMPILER_FLAGS=OFF \
    -DBUILD_SHARED_AND_STATIC_LIBS=On \
"

do_install_append() {

	# Create a symlink to support RDKB components which expect cJSON.h to
	# be found in the toplevel sysroot ${includedir} rather than within the
	# cjson subdirectory. Fixme: The real solution would be to fix those
	# recipes so that this symlink is not required.

	ln -s cjson/cJSON.h ${D}${includedir}/cJSON.h
}

BBCLASSEXTEND = "native nativesdk"
