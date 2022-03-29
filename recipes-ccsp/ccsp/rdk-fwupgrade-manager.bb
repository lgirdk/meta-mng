SUMMARY = "RDK Firmware Upgrade Manager"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia hal-cm hal-fwupgrade hal-platform libunpriv"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

do_install_append () {
	install -d ${D}/usr/rdk/fwupgrademanager
	install -m 644 ${S}/config/RdkFwUpgradeManager.xml ${D}/usr/rdk/fwupgrademanager/
	ln -sf ${bindir}/fwupgrademanager ${D}/usr/rdk/fwupgrademanager/fwupgrademanager
}

FILES_${PN} += "/usr/rdk/fwupgrademanager"
