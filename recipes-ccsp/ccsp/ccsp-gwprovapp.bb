SUMMARY = "CCSP GWProvAPP"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "hal-cm hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi utopia cimplog"
DEPENDS += "telemetry"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/gwprovapp.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

CFLAGS += " \
    ${@bb.utils.contains('DISTRO_FEATURES','bci','-DCISCO_CONFIG_TRUE_STATIC_IP -DCISCO_CONFIG_DHCPV6_PREFIX_DELEGATION','',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES','bci','','-DFEATURE_SUPPORT_ONBOARD_LOGGING',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES','dslite','-DDSLITE_FEATURE_SUPPORT','',d)} \
"

LDFLAGS += " \
    -ltelemetry_msgsender \
    -lcimplog \
    -pthread \
"

do_install_append () {
	install -d ${D}/${includedir}
	install -m 644 ${S}/source/include/Tr69_Tlv.h ${D}/${includedir}/
}

FILES_${PN} += "/usr/ccsp"

RPROVIDES_${PN} += "${PN}-ccsp"