
SRC_URI += "file://private-nets_1.6.22.patch \
            file://01-ltmain.sh_1.6.22.patch \
            file://02-wecb_1.6.22.patch \
            file://ixml_header01.patch \
"

EXTRA_OECONF += " \
    --enable-ipv6 \
    --disable-blocking_tcp_connections \
    --disable-notification_reordering \
    --disable-static \
"
