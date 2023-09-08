require dnsmasq.inc

SRC_URI[md5sum] = "227fd0e81a5ed8134e1f60e175324c99"
SRC_URI[sha256sum] = "28d52cfc9e2004ac4f85274f52b32e1647b4dbc9761b82e7de1e41c49907eb08"
SRC_URI += "\
    file://lua.patch \
"
