SUMMARY = "RDK Telco Voice Manager"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "utopia json-hal-lib libparodus wrp-c trower-base64 nanomsg libunpriv avro-c util-linux"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

export ISRDKB_VOICE_DM_TR104_V2 = "${@bb.utils.contains('DISTRO_FEATURES','rdkb_voice_manager_dmltr104_v2','true','false',d)}"

CFLAGS += " \
    ${@bb.utils.contains('DISTRO_FEATURES','rdkb_voice_manager_dmltr104_v2','-DFEATURE_RDKB_VOICE_DM_TR104_V2=ON','',d)} \
"

CFLAGS += " \
    -I${STAGING_INCDIR}/libparodus \
"

do_install_append () {
	install -d ${D}/usr/rdk/voicemanager
	install -m 644 ${S}/config/telcovoice_config_default.json ${D}/usr/rdk/voicemanager/telcovoice_config_default.json
	ln -sf ${bindir}/telcovoice_manager ${D}/usr/rdk/voicemanager/telcovoice_manager

	install -d ${D}${sysconfdir}/rdk/conf
	install -m 644 ${S}/config/telcovoice_manager_conf.json ${D}${sysconfdir}/rdk/conf/

	if [ "${@bb.utils.contains('DISTRO_FEATURES','rdkb_voice_manager_dmltr104_v2','true','false',d)}" = "true" ]
	then
		install -d ${D}/${sysconfdir}/rdk/schemas
		install -m 644 ${S}/hal_schema/telcovoice_hal_schema_v2.json ${D}/${sysconfdir}/rdk/schemas/telcovoice_hal_schema.json
		install -m 644 ${S}/config/RdkTelcoVoiceManager_v2.xml ${D}/usr/rdk/voicemanager/RdkTelcoVoiceManager.xml
	else
		install -d ${D}/${sysconfdir}/rdk/schemas
		install -m 644 ${S}/hal_schema/telcovoice_hal_schema_v1.json ${D}/${sysconfdir}/rdk/schemas/telcovoice_hal_schema.json
		install -m 644 ${S}/config/RdkTelcoVoiceManager_v1.xml ${D}/usr/rdk/voicemanager/RdkTelcoVoiceManager.xml
	fi

	install -d ${D}/usr/ccsp/harvester
	install -m 644 ${S}/source/TR-181/integration_src.shared/VoiceDiagnostics.avsc ${D}/usr/ccsp/harvester/
}

FILES_${PN} += "/usr/ccsp /usr/rdk/voicemanager"
