
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://always-use-erouter-for-DUID-generation.patch \
            file://dibbler-init.sh \
            file://dibbler-server-notify.sh \
            file://client-notify.sh \
            file://prepare_dhcpv6_config.sh \
            file://udhcpc.vendor_specific \
            file://client.conf \
"

# ----------------------------------------------------------------------------

# Dibbler is written in C++, therefore by default the dibbler packages all have
# a runtime dependency on libstdc++ (a ~1MB library on the target).
#
# If only one package in the final rootfs requires libstdc++ then linking that
# package statically with libstdc++ can save some space in the final rootfs
# image. If multiple packages need libstdc++ the linking them all dynamically
# with libstdc++ may be better.

# CPPFLAGS += "-ffunction-sections -fdata-sections"
# LDFLAGS += "-Wl,--gc-sections -static-libstdc++"

# ----------------------------------------------------------------------------

do_install_append () {

	install -d ${D}${base_libdir}/rdk
	install -m 755 ${WORKDIR}/dibbler-init.sh ${D}${base_libdir}/rdk/
	install -m 755 ${WORKDIR}/prepare_dhcpv6_config.sh ${D}${base_libdir}/rdk/

	install -d ${D}${sysconfdir}/dibbler

	# This is work in progress. As a temp solution, don't install
	# /etc/dibbler/server.conf to avoid clashes with the one installed by
	# the SDK.

	#ln -s /tmp/dibbler/server.conf ${D}${sysconfdir}/dibbler/server.conf
	install -m 755 ${WORKDIR}/dibbler-server-notify.sh ${D}${sysconfdir}/dibbler/dibbler-server-notify.sh

	install -m 755 ${WORKDIR}/client-notify.sh ${D}${sysconfdir}/dibbler/client-notify.sh


	install -m 644 ${WORKDIR}/udhcpc.vendor_specific ${D}${sysconfdir}/dibbler/udhcpc.vendor_specific
	install -m 644 ${WORKDIR}/client.conf ${D}${sysconfdir}/dibbler/client.conf-basic
	ln -s /tmp/dibbler/client.conf ${D}${sysconfdir}/dibbler/client.conf

	ln -s /tmp/dibbler/radvd.conf ${D}${sysconfdir}/dibbler/radvd.conf
}

FILES_${PN}-client += "${sysconfdir}/dibbler/client.conf-basic ${sysconfdir}/dibbler/client-notify.sh"

FILES_${PN}-server += "${sysconfdir}/dibbler ${base_libdir}/rdk"

RDEPENDS_${PN}-client += "bash"

RDEPENDS_${PN}-server += "bash"
