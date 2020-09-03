SUMMARY = "RBus core"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "rtmessage gtest benchmark"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/rbuscore.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig systemd

EXTRA_OECMAKE += "-DBUILD_RBUS=ON"

do_install_append() {

	# RBus is currently heavily dependent on systemd. Alternatives for the
	# scripts and services below will be needed for non-systemd builds.

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}
	then
		install -d ${D}${bindir}
		install -m 0755 ${S}/conf/rbus_log_capture.sh ${D}${bindir}/
	fi

	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${S}/conf/rbus.service ${D}${systemd_unitdir}/system/
	install -m 0644 ${S}/conf/rbus_session_mgr.service ${D}${systemd_unitdir}/system/
	install -m 0644 ${S}/conf/rbus_log.service ${D}${systemd_unitdir}/system/
	install -m 0644 ${S}/conf/rbus_monitor.path ${D}${systemd_unitdir}/system/
	install -m 0644 ${S}/conf/rbus_monitor.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_SERVICE_${PN} = "rbus.service rbus_session_mgr.service rbus_log.service rbus_monitor.path rbus_monitor.service"

FILES_${PN}-dev += "${libdir}/cmake"
