SUMMARY = "Mesh Agent"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require recipes-ccsp/ccsp/ccsp_common.inc

DEPENDS += "utopia trower-base64 libparodus telemetry"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/meshagent${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

CFLAGS += " \
    -DENABLE_MESH_SOCKETS \
    -I${STAGING_INCDIR}/libparodus \
    -I${STAGING_INCDIR}/trower-base64 \
"

DATAMODEL_XML = "config/TR181-MeshAgent.xml"

do_install_append () {
	install -d ${D}/usr/include/mesh
	install -m 644 ${S}/source/include/*.h ${D}/usr/include/mesh/

	install -d ${D}${sysconfdir}
	install -m 755 ${S}/scripts/plume_init.sh ${D}${sysconfdir}/

	install -d ${D}/usr/ccsp/mesh
	install -m 644 ${S}/config/MeshAgent.cfg ${D}/usr/ccsp/mesh/
	install -m 644 ${S}/config/MeshAgentDM.cfg ${D}/usr/ccsp/mesh/
	install -m 755 ${S}/scripts/active_host_filter.sh ${D}/usr/ccsp/mesh/

	ln -sf /usr/bin/meshAgent ${D}/usr/ccsp/mesh/meshAgent
}

FILES_${PN} += "/usr/ccsp"

# active_host_filter.sh is a bash script
RDEPENDS_${PN}_append = " bash"
