SUMMARY = "RDK VLAN Manager"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia hal-platform json-hal-lib libunpriv"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

DATAMODEL_XML = "config/RdkVlanManager.xml"

do_install_append () {
	install -d ${D}${sysconfdir}/rdk/conf
	install -m 644 ${S}/config/vlan_manager_conf.json ${D}${sysconfdir}/rdk/conf/

	install -d ${D}/${sysconfdir}/rdk/schemas
	install -m 644 ${S}/hal_schema/ethlinkvlanterm_hal_schema.json ${D}/${sysconfdir}/rdk/schemas/
}
