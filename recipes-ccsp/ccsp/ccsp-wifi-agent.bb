SUMMARY = "CCSP WiFi Agent"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia hal-wifi telemetry avro-c cimplog libparodus util-linux"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES','systemd','systemd','',d)}"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig systemd

EXTRA_OECONF += "${CCSP_CONFIG_ARCH} ${CCSP_CONFIG_PLATFORM}"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES','systemd','--enable-notify','',d)}"

CFLAGS += " \
    -I${STAGING_INCDIR}/cimplog \
    -I${STAGING_INCDIR}/libparodus \
    ${@bb.utils.contains('DISTRO_FEATURES','meshwifi','-DENABLE_FEATURE_MESHWIFI','',d)} \
"

LDFLAGS += " \
    -lcimplog \
    -llibparodus \
    -ltelemetry_msgsender \
    -lpthread \
"

do_compile_prepend () {
	( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config-atom/TR181-WiFi-USGv2.XML ${S}/source/WifiSsp/dm_pack_datamodel.c )
}

do_install_append () {

	install -d ${D}/usr/ccsp/wifi

	install -m 755 ${S}/scripts/aphealth.sh ${D}/usr/ccsp/wifi/
	install -m 755 ${S}/scripts/aphealth_log.sh ${D}/usr/ccsp/wifi/
	install -m 755 ${S}/scripts/stahealth_log.sh ${D}/usr/ccsp/wifi/
	install -m 755 ${S}/scripts/br0_ip.sh ${D}/usr/ccsp/wifi/
	install -m 755 ${S}/scripts/br106_addvlan.sh ${D}/usr/ccsp/wifi/
	install -m 755 ${S}/scripts/lfp.sh ${D}/usr/ccsp/wifi/
	install -m 755 ${S}/scripts/process_monitor_atom.sh ${D}/usr/ccsp/wifi/
	install -m 755 ${S}/scripts/wifivAPPercentage.sh ${D}/usr/ccsp/wifi/

	if ${@bb.utils.contains('DISTRO_FEATURES','meshwifi','true','false',d)}; then
		install -m 755 ${S}/scripts/handle_mesh ${D}/usr/ccsp/wifi/
		install -m 755 ${S}/scripts/mesh_aclmac.sh ${D}/usr/ccsp/wifi/
		install -m 755 ${S}/scripts/mesh_setip.sh ${D}/usr/ccsp/wifi/
		install -m 755 ${S}/scripts/mesh_status.sh ${D}/usr/ccsp/wifi/
		install -m 755 ${S}/scripts/meshapcfg.sh ${D}/usr/ccsp/wifi/
	fi

	install -m 644 ${S}/config-atom/CcspWifi.cfg ${D}/usr/ccsp/wifi/
	install -m 644 ${S}/config-atom/CcspDmLib.cfg ${D}/usr/ccsp/wifi/
	install -m 644 ${S}/config-atom/WifiSingleClient.avsc ${D}/usr/ccsp/wifi/
	install -m 644 ${S}/config-atom/WifiSingleClientActiveMeasurement.avsc ${D}/usr/ccsp/wifi/

	install -d ${D}${includedir}/ccsp
	install -m 644 ${S}/source/TR-181/sbapi/*.h ${D}${includedir}/ccsp/

	install -d ${D}${includedir}/middle_layer_src/wifi
	install -m 644 ${S}/include/TR-181/ml/*.h ${D}${includedir}/middle_layer_src/wifi/

	install -d ${D}${systemd_unitdir}/system
	install -D -m 644 ${S}/scripts/systemd/wifi-telemetry.target ${D}${systemd_unitdir}/system/
	install -D -m 644 ${S}/scripts/systemd/wifi-telemetry-cron.service ${D}${systemd_unitdir}/system/
	install -D -m 644 ${S}/scripts/systemd/wifi-telemetry-sta.service ${D}${systemd_unitdir}/system/

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -D -m 755 ${S}/scripts/wifiTelemetrySetup.sh ${D}/usr/ccsp/wifi/wifiTelemetrySetup.sh
	fi
}

SYSTEMD_SERVICE_${PN} += "wifi-telemetry.target wifi-telemetry-cron.service wifi-telemetry-sta.service"

FILES_${PN} += "/usr/ccsp"

# libwifi.so is loaded as a plugin (ie using dlopen()) so the .so symlink needs
# to be in the main package. Fixme: libraries loaded as plugins shouldn't be
# installed directly into /usr/lib

FILES_${PN} += "${libdir}/libwifi.so"
