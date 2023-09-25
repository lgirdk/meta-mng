SUMMARY = "RDK WAN Manager"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "ccsp-misc utopia hal-cm hal-dhcpv4c libunpriv nanomsg"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

PACKAGECONFIG ?= "${@bb.utils.contains('DISTRO_FEATURES', 'gpon', 'ipoe-health-check', '', d)}"

PACKAGECONFIG[ipoe-health-check] = "--enable-ipoehc,--disable-ipoehc,,ipoe-health-check"

# Fixme: It's not clear what FEATURE_802_1P_COS_MARKING is but the
#        code doesn't build if this option is not enabled.

CFLAGS += " \
    -DFEATURE_802_1P_COS_MARKING \
"

DATAMODEL_XML = "config/RdkWanManager_v2.xml"

RDEPENDS_${PN} += "ndisc6-rdisc6"
