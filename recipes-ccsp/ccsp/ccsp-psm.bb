SUMMARY = "CCSP Persistent Storage Manager"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia libsyswrapper libunpriv cjson rbus rbus-core"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES','systemd','systemd','',d)}"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES','systemd','--enable-notify','',d)}"

# ----------------------------------------------------------------------------

# Setting these (e.g. from a platform specific ccsp-psm.bbappend) will cause
# the values in the installed bbhm_def_cfg.xml to be updated.

CCSP_PSM_PRODUCTCLASS ??= ""
CCSP_PSM_MANUFACTUREROUI ??= ""

# ----------------------------------------------------------------------------

do_install_append () {

	install -d ${D}/usr/ccsp/config
	install -m 644 ${S}/config/bbhm_def_cfg_qemu.xml ${D}/usr/ccsp/config/bbhm_def_cfg.xml

	if [ -n "${CCSP_PSM_PRODUCTCLASS}" ]; then
		sed -i '/dmsb\.device\.deviceinfo\.ProductClass/s/>[^<]\+</>${CCSP_PSM_PRODUCTCLASS}</' ${D}/usr/ccsp/config/bbhm_def_cfg.xml
	fi

	if [ -n "${CCSP_PSM_MANUFACTUREROUI}" ]; then
		sed -i '/dmsb\.device\.deviceinfo\.ManufacturerOUI/s/>[^<]\+</>${CCSP_PSM_MANUFACTUREROUI}</' ${D}/usr/ccsp/config/bbhm_def_cfg.xml
	fi

	ln -sf ${bindir}/PsmSsp ${D}/usr/ccsp/PsmSsp
}

FILES_${PN} += "/usr/ccsp"
