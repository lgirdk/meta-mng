
# ----------------------------------------------------------------------------

DEPENDS += "rdk-logger"
CFLAGS += "-DFEATURE_SUPPORT_RDKLOG"
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
    -D_ANSC_AES_USED_ \
    -D_ANSC_IPV6_COMPATIBLE_ \
    -D_ANSC_LINUX \
    -D_ANSC_USER \
    -D_ANSC_USE_OPENSSL_ \
    -D_BBHM_SSE_FILE_IO \
    -D_CCSP_CWMP_TCP_CONNREQ_HANDLER \
    -D_COSA_FOR_COMCAST_ \
    -D_COSA_HAL_ \
    -D_DEBUG \
    -D_DSLH_STUN_ \
    -D_NO_ANSC_ZLIB_ \
    -D_NO_EXECINFO_H_ \
    -D_NO_PKI_KB5_SUPPORT \
    -DBUILD_WEB \
    -DCONFIG_SYSTEM_MOCA \
    -DENABLE_SA_KEY \
    -DFEATURE_SUPPORT_SYSLOG \
    -DMOCA_HOME_ISOLATION \
    -DNTPD_ENABLE \
    -DUSE_NOTIFY_COMPONENT \
    -DUTC_ENABLE \
    -U_COSA_SIM_ \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bci', '-D_COSA_FOR_BCI_', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bci', '', '-DFEATURE_SUPPORT_ONBOARD_LOGGING', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ddns_broadband', '-DDDNS_BROADBANDFORUM', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'dslite', '-DDSLITE_FEATURE_SUPPORT', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'interworking', '-DFEATURE_SUPPORT_INTERWORKING', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'meshwifi', '-DFEATURE_SUPPORT_MESH', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'multilan', '-DMULTILAN_FEATURE', '', d)} \
"

CFLAGS += "-DSAFEC_DUMMY_API"

# ----------------------------------------------------------------------------

CFLAGS += "${CCSP_CFLAGS_MACHINE}"

include recipes-ccsp/ccsp/ccsp_soc.inc
include recipes-ccsp/ccsp/ccsp_oem.inc

# ----------------------------------------------------------------------------

FILES_${PN} += "${libdir}/*.so"
FILES_SOLIBSDEV = ""
INSANE_SKIP_${PN} += "dev-so"

# ----------------------------------------------------------------------------
