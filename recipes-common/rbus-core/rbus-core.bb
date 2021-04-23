SUMMARY = "RBus core"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=075c59e772e98d304efd052108da3bd7"

DEPENDS = "rtmessage msgpack-c gtest benchmark"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/rbuscore${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig systemd

do_install_append() {

	# RBus is currently heavily dependent on systemd. Alternatives for the
	# scripts and services below will be needed for non-systemd builds.

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}
	then
		install -d ${D}${bindir}
		install -m 0755 ${S}/conf/rbus_log_capture.sh ${D}${bindir}/

		install -d ${D}${systemd_unitdir}/system/rbus.service.d
		install -m 0644 ${S}/conf/rbus_rdkb.conf ${D}${systemd_unitdir}/system/rbus.service.d/
	fi

	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${S}/conf/rbus.service ${D}${systemd_unitdir}/system/
	install -m 0644 ${S}/conf/rbus_session_mgr.service ${D}${systemd_unitdir}/system/
	install -m 0644 ${S}/conf/rbus_log.service ${D}${systemd_unitdir}/system/
	install -m 0644 ${S}/conf/rbus_monitor.path ${D}${systemd_unitdir}/system/
	install -m 0644 ${S}/conf/rbus_monitor.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_SERVICE_${PN} = "rbus.service rbus_session_mgr.service rbus_log.service rbus_monitor.path rbus_monitor.service"

FILES_${PN} += "${systemd_unitdir}/system/rbus.service.d/rbus_rdkb.conf"

FILES_${PN}-dev += "${libdir}/cmake"
