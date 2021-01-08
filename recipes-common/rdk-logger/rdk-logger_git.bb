SUMMARY = "RDK Logger"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "log4c rdklist"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

PACKAGECONFIG ?= "milestone ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd-syslog-helper', '', d)}"

PACKAGECONFIG[milestone] = "--enable-milestone,,"
PACKAGECONFIG[systemd-syslog-helper] = "--enable-systemd-syslog-helper,,syslog-helper systemd"

# Note: Source file modifications should be done as part of do_patch (rather
# than as part of do_configure) to avoid potential problems with sstate cache.

do_use_rdkb_config_files() {
	cp ${S}/rdkb_log4crc ${S}/log4crc
	cp ${S}/rdkb_debug.ini ${S}/debug.ini
}

do_patch_append() {
    # This is python, not shell, so indent with 4 spaces
    bb.build.exec_func('do_use_rdkb_config_files', d)
}

do_install_append () {

	# The only thing currently using the logMilestone.sh script is the
	# busybox udhcpc client from meta-rdk-ext. Similar changes may be
	# needed to other dhcp clients if the meta-rdk-ext busybox config files
	# etc are not used...

	install -d ${D}${base_libdir}/rdk
	install -m 0755 ${S}/scripts/logMilestone.sh ${D}${base_libdir}/rdk/

	# Legacy symlink so that /etc/log4crc can be accessed via /rdklogger/log4crc
	# To be removed once all users of /rdklogger/log4crc have been updated.

	install -d ${D}/rdklogger
	ln -sf /etc/log4crc ${D}/rdklogger/log4crc
}

FILES_${PN} += "${base_libdir}/rdk /rdklogger"
