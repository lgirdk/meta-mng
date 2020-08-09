FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://rtadv.patch"

INHIBIT_UPDATERCD_BBCLASS = "1"

SYSTEMD_AUTO_ENABLE = "disable"

# Various over-rides for default configure options (temp solution)

EXTRA_OECONF_append = " \
    --disable-doc \
    --disable-bgpd \
    --disable-ospfd \
    --disable-ospf6d \
    --disable-ospfclient \
    --disable-vtysh \
    --disable-watchquagga \
    --enable-user=root \
    --enable-group=root \
    --enable-vty-group=root \
    --localstatedir=/var/run \
"

RDEPENDS_${PN}_remove = "${PN}-bgpd ${PN}-isisd ${PN}-ospf6d ${PN}-ospfd ${PN}-ripd ${PN}-ripngd"
