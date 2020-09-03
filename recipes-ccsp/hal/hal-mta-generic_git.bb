SUMMARY = "Generic version of CCSP MTA HAL"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "halinterface"

PROVIDES = "hal-mta"
RPROVIDES_${PN} = "hal-mta"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/hal.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH};name=mtahal"

SRCREV_mtahal ?= "${AUTOREV}"
SRCREV_FORMAT = "mtahal"

S = "${WORKDIR}/git/source/mta"

inherit autotools

CFLAGS += "-I${STAGING_INCDIR}/ccsp"
