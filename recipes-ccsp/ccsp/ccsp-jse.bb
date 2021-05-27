SUMMARY = "Server side javascript engine based on Duktape and qDecoder"
LICENSE = "Apache-2.0 & MIT & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e76996dff7c96f34b60249db92fc7aeb"

DEPENDS = "ccsp-common-library dbus duktape libxml2 openssl qdecoder"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

PACKAGECONFIG ?= "${@bb.utils.contains('DISTRO_FEATURES', 'fcgi', 'fcgi', '', d)}"

PACKAGECONFIG[fcgi] = "-DFAST_CGI=ON,,fcgi"

EXTRA_OECMAKE += "-DBUILD_RDK=ON -DENABLE_LIBCRYPTO=ON -DENABLE_LIBXML2=ON"

CFLAGS += " \
    -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
    -I${STAGING_INCDIR}/duktape \
    -I${STAGING_INCDIR}/libxml2 \
"

LDFLAGS += " \
    -ldbus-1 \
"

CFLAGS += "-ffunction-sections -fdata-sections"
LDFLAGS += "-Wl,--gc-sections"

do_install() {
	install -d ${D}${bindir}
	install -m 755 jse ${D}${bindir}/
}

FILES_${PN} += "/www"
