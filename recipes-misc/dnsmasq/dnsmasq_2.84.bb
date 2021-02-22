require dnsmasq.inc

SRC_URI[md5sum] = "1875b70e98569f5d0abd84b78b816d31"
SRC_URI[sha256sum] = "603195c64b73137609b07e1024ae0b37f652b2f5fe467dce66985b3d1850050c"
SRC_URI += "\
    file://lua.patch \
"

