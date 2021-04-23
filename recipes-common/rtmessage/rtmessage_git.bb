SUMMARY = "RBus low level messaging library"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "cjson rdk-logger"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE += "-DRDKC_BUILD=OFF -DBUILD_DATAPROVIDER_LIB=OFF -DBUILD_DMCLI=OFF -DBUILD_DMCLI_SAMPLE_APP=OFF"

FILES_${PN}-dev += "${libdir}/cmake"

# The librtMessage.so shared lib isn't versioned, so force the .so file into
# the run-time package (and keep it out of the -dev package).

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so"
