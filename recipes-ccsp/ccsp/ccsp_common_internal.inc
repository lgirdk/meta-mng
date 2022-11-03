
MIRRORS = "${MIRRORS_PRIVATE}"

# ----------------------------------------------------------------------------

DEPENDS += "ccsp-common-library-native"

# ----------------------------------------------------------------------------

DEPENDS += "rdk-logger"
CFLAGS += "-DFEATURE_SUPPORT_RDKLOG -DDISABLE_LOGAGENT"
LDFLAGS += "-lrdkloggers"

# ----------------------------------------------------------------------------

DEPENDS += "dbus"
CFLAGS += "-I${STAGING_INCDIR}/dbus-1.0 -I${STAGING_LIBDIR}/dbus-1.0/include"
LDFLAGS += "-ldbus-1"

# ----------------------------------------------------------------------------

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'breakpad', 'breakpad-wrapper', '', d)}"
CFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'breakpad', '-DINCLUDE_BREAKPAD', '', d)}"
LDFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'breakpad', '-lbreakpadwrapper', '', d)}"

CXXFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'breakpad', '-DINCLUDE_BREAKPAD', '', d)}"

# ----------------------------------------------------------------------------

# This list of CFLAGS should be common to all targets. Any SOC or
# machine specific flags should be defined via CCSP_CFLAGS_MACHINE.

CFLAGS += " \
    -D_LG_OFW_ \
    -D_ANSC_AES_USED_ \
    -D_ANSC_IPV6_COMPATIBLE_ \
    -D_ANSC_USE_OPENSSL_ \
    -D_CCSP_CWMP_TCP_CONNREQ_HANDLER \
    -D_DEBUG \
    -D_NO_ANSC_ZLIB_ \
    -D_NO_EXECINFO_H_ \
    -D_NO_PKI_KB5_SUPPORT \
    -DBUILD_WEB \
    -DFEATURE_SUPPORT_SYSLOG \
    -DNDEBUG \
    -DNTPD_ENABLE \
    -DUSE_NOTIFY_COMPONENT \
    -DUTC_ENABLE \
    -U_COSA_SIM_ \
    ${@bb.utils.contains('DISTRO_FEATURES', 'autowan', '-DAUTOWAN_ENABLE', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bci', '-D_COSA_FOR_BCI_', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bci', '', '-DFEATURE_SUPPORT_ONBOARD_LOGGING', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ddns_broadband', '-DDDNS_BROADBANDFORUM -DDDNS_SERVICE_BIN', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'dslite', '-DDSLITE_FEATURE_SUPPORT', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'fwupgrade_manager', '-DFEATURE_FWUPGRADE_MANAGER', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'gpon', '-DFEATURE_GPON', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'interworking', '-DFEATURE_SUPPORT_INTERWORKING', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'meshwifi', '-DFEATURE_SUPPORT_MESH -DENABLE_FEATURE_MESHWIFI', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'moca', '-DCONFIG_SYSTEM_MOCA -DMOCA_HOME_ISOLATION', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'multilan', '-DMULTILAN_FEATURE', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_telcovoice_manager', '-DFEATURE_RDKB_TELCOVOICE_MANAGER', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', '-DFEATURE_RDKB_WAN_MANAGER -D_WAN_MANAGER_ENABLED_', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', '-DCORE_NET_LIB', '', d)} \
"

CFLAGS += "-DSAFEC_DUMMY_API"
CXXFLAGS += "-DSAFEC_DUMMY_API"

# ----------------------------------------------------------------------------

CFLAGS += "${CCSP_CFLAGS_MACHINE}"

include recipes-ccsp/ccsp/ccsp_soc.inc
include recipes-ccsp/ccsp/ccsp_oem.inc


# ----------------------------------------------------------------------------

DATAMODEL_XML ?= ""

do_compile_prepend () {

	if [ -n "${DATAMODEL_XML}" ] ; then
		x=$(find ${S} -name Makefile.am | xargs grep dm_pack_datamodel.c | head -n1)
		if [ -z "$x" ] ; then
			echo "DATAMODEL_XML defined but dm_pack_datamodel.c not found in any Makefile.am"
			exit 1
		fi
		dmpath=""
		for y in $x ; do
			if [ "$(basename $y)" = "dm_pack_datamodel.c" ] ; then
				dmpath=${x%%/Makefile.am:*}/$(dirname $y)
				break
			fi
		done

		# Clear all flags first, mainly as a workaround for badly behaved SOC
		# SDKs which force their own hardcoded defines into .XML files

		sed -e '/<?define AUTOWAN_ENABLE=True?>/d' \
		    -e '/<?define DDNS_BROADBANDFORUM=True?>/d' \
		    -e '/<?define DSLITE_FEATURE_SUPPORT=True?>/d' \
		    -e '/<?define FEATURE_FWUPGRADE_MANAGER=True?>/d' \
		    -e '/<?define FEATURE_SUPPORT_INTERWORKING=True?>/d' \
		    -e '/<?define FEATURE_RDKB_AUTO_PORT_SWITCH=True?>/d' \
		    -e '/<?define FEATURE_RDKB_WAN_MANAGER=True?>/d' \
		    -e '/<?define FEATURE_RDKB_WAN_UPSTREAM=True?>/d' \
		    -i ${S}/${DATAMODEL_XML}

		for f in ${@bb.utils.contains('DISTRO_FEATURES', 'autowan', 'AUTOWAN_ENABLE', '', d)} \
			 ${@bb.utils.contains('DISTRO_FEATURES', 'ddns_broadband', 'DDNS_BROADBANDFORUM', '', d)} \
			 ${@bb.utils.contains('DISTRO_FEATURES', 'dslite', 'DSLITE_FEATURE_SUPPORT', '', d)} \
			 ${@bb.utils.contains('DISTRO_FEATURES', 'fwupgrade_manager', 'FEATURE_FWUPGRADE_MANAGER', '', d)} \
			 ${@bb.utils.contains('DISTRO_FEATURES', 'interworking', 'FEATURE_SUPPORT_INTERWORKING', '', d)} \
			 ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_auto_port_switch', 'FEATURE_RDKB_AUTO_PORT_SWITCH', '', d)} \
			 ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', 'FEATURE_RDKB_WAN_MANAGER', '', d)} \
			 ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_upstream', 'FEATURE_RDKB_WAN_UPSTREAM', '', d)}
		do
			sed "2i <?define $f=True?>" -i ${S}/${DATAMODEL_XML}
		done

		python3 ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/${DATAMODEL_XML} $dmpath/dm_pack_datamodel.c
	fi
}

# ----------------------------------------------------------------------------

FILES_${PN} += "${libdir}/*.so"
FILES_SOLIBSDEV = ""
INSANE_SKIP_${PN} += "dev-so"

# ----------------------------------------------------------------------------
