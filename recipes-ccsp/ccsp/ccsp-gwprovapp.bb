SUMMARY = "CCSP GWProvAPP"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia telemetry halinterface hal-cm"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/gwprovapp${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

CFLAGS += " \
    ${@bb.utils.contains('DISTRO_FEATURES','bci','-DCISCO_CONFIG_TRUE_STATIC_IP -DCISCO_CONFIG_DHCPV6_PREFIX_DELEGATION','',d)} \
"

FILES_${PN} += "/usr/ccsp"
