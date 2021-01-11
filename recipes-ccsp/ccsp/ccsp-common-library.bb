SUMMARY = "CCSP Common Library"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=19774cd4dd519f099bc404798ceeab19"

require ccsp_common_internal.inc

DEPENDS += "openssl rbus-core zlib"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}.git;protocol=${LGI_RDKB_GIT_PROTOCOL};branch=${LGI_RDKB_GIT_BRANCH}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig systemd

CFLAGS += " \
    -D_GNU_SOURCE -D__USE_XOPEN \
"

LDFLAGS += " \
    -lrbus-core \
    -lrtMessage \
"

do_install_append () {

	install -d ${D}${includedir}/ccsp

	install -m 644 ${S}/source/ccsp/components/common/MessageBusHelper/include/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/ccsp/components/common/PoamIrepFolder/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/ccsp/components/include/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/ccsp/custom/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/ccsp/include/*.h ${D}${includedir}/ccsp/

	install -m 644 ${S}/source/cosa/include/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/cosa/include/linux/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/cosa/package/slap/include/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/cosa/package/system/include/*.h ${D}${includedir}/ccsp/

	install -m 644 ${S}/source/debug_api/include/*.h ${D}${includedir}/ccsp/

	install -m 644 ${S}/source/dm_pack/dm_pack_create_func.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/dm_pack/dm_pack_xml_helper.h ${D}${includedir}/ccsp/

	install -m 644 ${S}/source/util_api/ansc/include/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/util_api/asn.1/include/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/util_api/http/include/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/util_api/slap/components/SlapVarConverter/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/util_api/stun/include/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/util_api/tls/include/*.h ${D}${includedir}/ccsp/
	install -m 644 ${S}/source/util_api/web/include/*.h ${D}${includedir}/ccsp/

	install -m 644 ${S}/WebConfig_Framework/*.h ${D}${includedir}/ccsp/

	# Fixme: these files are installed to both /usr/include/ccsp and /usr/include/ccsp/linux ?
	install -d ${D}${includedir}/ccsp/linux
	install -m 644 ${S}/source/cosa/include/linux/*.h ${D}${includedir}/ccsp/linux

	install -d ${D}${sysconfdir}/ccsp
	install -m 755 ${S}/scripts/cosa ${D}${sysconfdir}/ccsp/

	install -d ${D}/usr/ccsp
	install -m 755 ${S}/scripts/ccsp_restart.sh ${D}/usr/ccsp/
	install -m 755 ${S}/scripts/cli_start_arm.sh ${D}/usr/ccsp/cli_start.sh
	install -m 755 ${S}/scripts/cosa_start_arm.sh ${D}/usr/ccsp/cosa_start.sh
	install -m 755 ${S}/scripts/cosa_start_rem.sh ${D}/usr/ccsp/cosa_start_rem.sh
	install -m 755 ${S}/scripts/cosa_stop.sh ${D}/usr/ccsp/
	install -m 755 ${S}/scripts/rbusFlagSync.sh ${D}/usr/ccsp/
	install -m 755 ${S}/scripts/rbus_status_logger.sh ${D}/usr/ccsp/

	install -m 644 ${S}/source/ccsp/components/CCSP_AliasMgr/custom_mapper.xml ${D}/usr/ccsp/

	install -d ${D}/usr/ccsp/cm
	install -d ${D}/usr/ccsp/mta
	install -d ${D}/usr/ccsp/pam
	install -d ${D}/usr/ccsp/tr069pa

	install -m 644 ${S}/config/basic.conf ${D}/usr/ccsp/
	install -m 644 ${S}/config/ccsp_msg.cfg ${D}/usr/ccsp/
	install -m 644 ${S}/config/ccsp_msg.cfg ${D}/usr/ccsp/cm/
	install -m 644 ${S}/config/ccsp_msg.cfg ${D}/usr/ccsp/mta/
	install -m 644 ${S}/config/ccsp_msg.cfg ${D}/usr/ccsp/pam/
	install -m 644 ${S}/config/ccsp_msg.cfg ${D}/usr/ccsp/tr069pa/

	install -d ${D}/lib/rdk
	install -m 755 ${S}/scripts/rbus_rfc_handler.sh ${D}/lib/rdk/rbus_rfc_handler.sh
	install -m 755 ${S}/scripts/rbus_termination_handler.sh ${D}/lib/rdk/rbus_termination_handler.sh
}

FILES_${PN} += "/usr/ccsp /lib/rdk"

RPROVIDES_${PN} += "ccsp-common-startup"
