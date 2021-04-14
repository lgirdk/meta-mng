SUMMARY = "CCSP P and M"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia hal-cm hal-dhcpv4c hal-ethsw hal-mso_mgmt ccsp-lm-lite telemetry ccsp-hotspot ccsp-misc cimplog cjson curl libparodus libsyswrapper msgpack-c nanomsg trower-base64 wrp-c"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES','systemd','systemd','',d)}"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF += "${CCSP_CONFIG_ARCH} ${CCSP_CONFIG_PLATFORM}"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES','systemd','--enable-notify','',d)}"

CFLAGS += " \
    -I${STAGING_INCDIR}/cimplog \
    -I${STAGING_INCDIR}/cjson \
    -I${STAGING_INCDIR}/libparodus \
    -I${STAGING_INCDIR}/msgpackc \
    -I${STAGING_INCDIR}/nanomsg \
    -I${STAGING_INCDIR}/syscfg \
    -I${STAGING_INCDIR}/trower-base64 \
    -I${STAGING_INCDIR}/ulog \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
    -I${STAGING_INCDIR}/wrp-c \
"

CFLAGS += " \
    -DCONFIG_VENDOR_CUSTOMER_COMCAST -DCONFIG_INTERNET2P0 -DCONFIG_CISCO_HOTSPOT \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bci', '-DCISCO_CONFIG_TRUE_STATIC_IP -DCISCO_CONFIG_DHCPV6_PREFIX_DELEGATION -DCONFIG_CISCO_TRUE_STATIC_IP -D_BCI_FEATURE_REQ', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'meshwifi', '-DENABLE_FEATURE_MESHWIFI', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'dslite', '-DDSLITE_FEATURE_SUPPORT', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wbCfgTestApp', '-DWEBCFG_TEST_SIM', '', d)} \
"

CFLAGS += " \
    -DCONFIG_MNG \
    -D_DHCPV6_DEFAULT_STATELESS_ \
    -D_DISABLE_WIFI_HEALTH_STATS_TO_NVRAM_ \
"

LDFLAGS += " \
    -lutctx \
    -lutapi \
    -lcjson \
    -llibparodus \
    -lnanomsg \
    -lwrp-c \
    -lmsgpackc \
    -ltrower-base64 \
    -lcimplog \
    -lsysevent \
    -ltelemetry_msgsender \
    -lsyscfg \
    -lpthread \
    -lm \
    -lrt \
"

do_compile_prepend () {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'bci', 'true', 'false', d)}; then
		( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config-arm/TR181-USGv2_bci.XML ${S}/source/PandMSsp/dm_pack_datamodel.c )
	else
		( /usr/bin/python ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config-arm/TR181-USGv2.XML ${S}/source/PandMSsp/dm_pack_datamodel.c )
	fi
}

do_install_append () {

	install -d ${D}${includedir}/ccsp
	install -m 644 ${S}/source/TR-181/include/*.h ${D}${includedir}/ccsp/
	install -d ${D}${includedir}/middle_layer_src/pam
	install -m 644 ${S}/source/TR-181/middle_layer_src/*.h ${D}${includedir}/middle_layer_src/pam/

	install -d ${D}${sysconfdir}
	install -m 644 ${S}/arch/intel_usg/boards/arm_shared/scripts/partners_defaults.json ${D}${sysconfdir}/
	install -m 644 ${S}/arch/intel_usg/boards/arm_shared/scripts/rfcDefaults.json ${D}${sysconfdir}/
	# Fixme: these scripts don't belong in /etc
	install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/AutoReboot.sh ${D}${sysconfdir}/
	install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/RebootCondition.sh ${D}${sysconfdir}/
	install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/ScheduleAutoReboot.sh ${D}${sysconfdir}/
	install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/calc_random_time_to_reboot_dev.sh ${D}${sysconfdir}/
	install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/network_response.sh ${D}${sysconfdir}/
	install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/redirect_url.sh ${D}${sysconfdir}/
	install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/restart_services.sh ${D}${sysconfdir}/
	install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/revert_redirect.sh ${D}${sysconfdir}/
	install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/start_lighttpd.sh ${D}${sysconfdir}/
	install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/whitelist.sh ${D}${sysconfdir}/

	install -d ${D}/usr/ccsp/pam
	install -m 644 ${S}/config-arm/CcspDmLib.cfg ${D}/usr/ccsp/pam/
	install -m 644 ${S}/config-arm/CcspPam.cfg ${D}/usr/ccsp/pam/
	install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/moca_status.sh ${D}/usr/ccsp/pam/
	install -m 755 ${S}/scripts/email_notification_monitor.sh ${D}/usr/ccsp/pam/
	install -m 755 ${S}/scripts/launch_tr69.sh ${D}/usr/ccsp/pam/
	install -m 755 ${S}/scripts/unique_telemetry_id.sh ${D}/usr/ccsp/pam/

	ln -sf ${bindir}/CcspPandMSsp ${D}/usr/ccsp/pam/CcspPandMSsp
}

FILES_${PN} += "/usr/ccsp"

# The network_response.sh script calls curl
RDEPENDS_${PN} += "curl"

# email_notification_monitor.sh, launch_adv_security.sh and launch_tr69.sh are bash scripts
RDEPENDS_${PN} += "bash"
