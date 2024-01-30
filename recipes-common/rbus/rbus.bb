SUMMARY = "RBus all-in-one"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ed63516ecab9f06e324238dd2b259549"

DEPENDS = "cjson linenoise msgpack-c rdk-logger"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/rbus2${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig systemd

EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Release -DMSG_ROUNDTRIP_TIME=ON -DENABLE_RDKLOGGER=ON"

# ccsp_common.inc is not included by this recipe, so machine specific
# CFLAGS etc need to be added manually.

CFLAGS += "${CCSP_CFLAGS_MACHINE}"

do_install_append() {

	# Fix include paths within rbus headers to match path in sysroot
	for f in ${D}${includedir}/rbus/*.h ${D}${includedir}/rtmessage/*.h ; do
		sed -e 's|^#include <rbus|#include <rbus/rbus|' \
		    -e 's|^#include <rtAdvisory.h>|#include <rtmessage/rtAdvisory.h>|' \
		    -e 's|^#include <rtConnection.h>|#include <rtmessage/rtConnection.h>|' \
		    -e 's|^#include <rtError.h>|#include <rtmessage/rtError.h>|' \
		    -e 's|^#include <rtLog.h>|#include <rtmessage/rtLog.h>|' \
		    -e 's|^#include <rtMemory.h>|#include <rtmessage/rtMemory.h>|' \
		    -e 's|^#include <rtMessageHeader.h>|#include <rtmessage/rtMessageHeader.h>|' \
		    -e 's|^#include <rtRetainable.h>|#include <rtmessage/rtRetainable.h>|' \
		    -e 's|^#include <rtVector.h>|#include <rtmessage/rtVector.h>|' \
		    -e 's|^#include <rtm_discovery_api.h>|#include <rtmessage/rtm_discovery_api.h>|' \
		    -i $f
	done

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
