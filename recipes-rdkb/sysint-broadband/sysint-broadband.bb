SUMMARY = "System Integration scripts for RDKB devices"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

# The legacy sysint-broadband recipe supported over-riding certain files from
# sysint-broadband with alternatives from a secondary repo checked out under
# ${S}/device. That approach was hard to maintain and has now been dropped.
# Platforms which need to modify sysint-broadband should instead do so in the
# conventional way (e.g. apply patches etc from a .bbappend).

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV  ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit systemd

do_install() {

	# --------------------------------------------------------------------

	install -d ${D}/rdklogger
	install -d ${D}${base_libdir}/rdk

	install -m 755 ${S}/*.sh ${D}/rdklogger/

	# The distinction between /rdklogger and /lib/rdk isn't very clear.
	# Some scripts are expected to be found in one path, some in the other.

	mv ${D}/rdklogger/apply_partner_customization.sh        ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/dcaSplunkUpload.sh                    ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/dca_utility.sh                        ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/DCMCronreschedule.sh                  ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/DCMscript.sh                          ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/getaccountid.sh                       ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/getip_file.sh                         ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/getipv6_container.sh                  ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/getpartnerid.sh                       ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/init-zram.sh                          ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/iptables_container.sh                 ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/ocsp-support.sh                       ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/postwanstatusevent.sh                 ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/rfc_refresh.sh                        ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/shortsDownload.sh                     ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/startStunnel.sh                       ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/startTunnel.sh                        ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/telemetryEventListener.sh             ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/utils.sh                              ${D}${base_libdir}/rdk/
	mv ${D}/rdklogger/wan_ssh.sh                            ${D}${base_libdir}/rdk/

	if ${@bb.utils.contains('DISTRO_FEATURES', 'snmppa', 'true', 'false', d)}
	then
		mv ${D}/rdklogger/handlesnmpv3.sh ${D}${base_libdir}/rdk/
	else
		rm ${D}/rdklogger/handlesnmpv3.sh
	fi

	# --------------------------------------------------------------------

	# Remove scripts which are Comcast specific or otherwise not applicable
	# for the generic RDKB.

	rm ${D}${base_libdir}/rdk/apply_partner_customization.sh
	rm ${D}${base_libdir}/rdk/getip_file.sh
	rm ${D}${base_libdir}/rdk/getipv6_container.sh
	rm ${D}${base_libdir}/rdk/iptables_container.sh
	rm ${D}${base_libdir}/rdk/shortsDownload.sh
	rm ${D}${base_libdir}/rdk/startStunnel.sh
	rm ${D}${base_libdir}/rdk/startTunnel.sh

	# --------------------------------------------------------------------

	install -d ${D}${sysconfdir}

	install -m 644 ${S}/etc/device.properties ${D}${sysconfdir}/

	if ${@bb.utils.contains('DISTRO_FEATURES', 'meshwifi', 'true', 'false', d)}
	then
		echo "MESH_SUPPORTED=true" >> ${D}${sysconfdir}/device.properties
	else
		echo "MESH_SUPPORTED=false" >> ${D}${sysconfdir}/device.properties
	fi

	echo "WAN_TYPE=DOCSIS" >> ${D}${sysconfdir}/device.properties

	install -m 644 ${S}/etc/dcm.properties ${D}${sysconfdir}/
	install -m 644 ${S}/etc/include.properties ${D}${sysconfdir}/
	install -m 644 ${S}/etc/telemetry2_0.properties ${D}${sysconfdir}/

	# log_timestamp.sh is a special case. It contains only the echo_t and
	# echo_et functions and is sourced by other scripts (ie never executed
	# directly).

	mv ${D}/rdklogger/log_timestamp.sh ${D}${sysconfdir}/

	# --------------------------------------------------------------------

	install -d ${D}${systemd_unitdir}/system
	install -m 644 ${S}/ocsp-support.service ${D}${systemd_unitdir}/system

	# --------------------------------------------------------------------
}

SYSTEMD_SERVICE_${PN} += "ocsp-support.service"

FILES_${PN} += "/rdklogger ${base_libdir}/rdk"
