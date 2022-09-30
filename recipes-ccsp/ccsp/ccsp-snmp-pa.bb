SUMMARY = "CCSP SNMP Protocol Adaptor"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2291535ca559c92189f5f6053018b3e2"

require ccsp_common.inc

DEPENDS += "utopia net-snmp openssl libsyswrapper"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

CFLAGS += " \
    ${@bb.utils.contains('DISTRO_FEATURES','snmppa','-DSNMP_PA_ENABLE','',d)} \
"

do_install_append () {
	if ${@bb.utils.contains('DISTRO_FEATURES','snmppa','true','false',d)}
	then
		install -d ${D}${sysconfdir}
		touch ${D}${sysconfdir}/SNMP_PA_ENABLE

		install -d ${D}/usr/ccsp/snmp
		install -m 644 ${S}/config/snmpd.conf ${D}/usr/ccsp/snmp/
		install -m 755 ${S}/scripts/run_snmpd.sh ${D}/usr/ccsp/snmp/
		install -m 755 ${S}/scripts/run_subagent.sh ${D}/usr/ccsp/snmp/
		install -m 644 ${S}/Mib2DmMapping/Ccsp*.xml ${D}/usr/ccsp/snmp/
		install -m 644 ${S}/Mib2DmMapping/DEVICE-WEBPA-MIB.xml ${D}/usr/ccsp/snmp/
		install -m 644 ${S}/Mib2DmMapping/SELFHEAL-DEVICE-MIB.xml ${D}/usr/ccsp/snmp/
		install -m 644 ${S}/Mib2DmMapping/XOPS-DEVICE-MGMT-MIB.xml ${D}/usr/ccsp/snmp/
	fi
}

FILES_${PN} += "/usr/ccsp"
