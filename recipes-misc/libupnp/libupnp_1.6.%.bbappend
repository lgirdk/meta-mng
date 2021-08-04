
SRC_URI += "file://01-ltmain.sh_1.6.22.patch \
            file://02-wecb_1.6.22.patch \
            file://ixml_header01.patch \
            file://fix-reply-to-unicast-discovery-message.patch \
            file://properly-format-string-for-server-header.patch \
            file://0001-CallStranger-Restrict-UPnP-SUBSCRIBE-NOTIFY-traffic.patch \
"

EXTRA_OECONF += " \
    --enable-ipv6 \
    --disable-blocking_tcp_connections \
    --disable-notification_reordering \
    --disable-static \
"
