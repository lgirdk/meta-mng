SUMMARY = "Generic version of CCSP DHCPv4 Client HAL"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "halinterface"

PROVIDES = "hal-dhcpv4c"
RPROVIDES_${PN} = "hal-dhcpv4c"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/hal${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git/source/dhcpv4c"

inherit autotools

CFLAGS += "-I${STAGING_INCDIR}/ccsp"
