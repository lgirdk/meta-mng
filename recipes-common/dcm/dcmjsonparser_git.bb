SUMMARY = "DCM json parser"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "cjson telemetry"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/dcm${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

CFLAGS_append = " \
    -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/cjson \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
"

LDFLAGS_append = " -ltelemetry_msgsender"

inherit autotools pkgconfig systemd

# Fixme: unnecessary, remove
RDEPENDS_${PN} = "cjson"
