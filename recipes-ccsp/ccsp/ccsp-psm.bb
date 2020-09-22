SUMMARY = "CCSP Persistent Storage Manager"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia cjson"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES','systemd','systemd','',d)}"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES','systemd','--enable-notify','',d)}"

LDFLAGS += " \
    -lcjson \
    -lpthread \
"

do_install_append () {
	install -d ${D}/usr/ccsp/psm
	install -m 755 ${S}/scripts/bbhm_patch.sh ${D}/usr/ccsp/psm/

	install -d ${D}/usr/ccsp/config
	install -m 644 ${S}/config/bbhm_def_cfg_qemu.xml ${D}/usr/ccsp/config/bbhm_def_cfg.xml

	ln -sf ${bindir}/PsmSsp ${D}/usr/ccsp/PsmSsp
}

FILES_${PN} += "/usr/ccsp"

RPROVIDES_${PN} += "${PN}-ccsp"
