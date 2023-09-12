require dnsmasq.inc

SRC_URI[md5sum] = "9513c4e6a6c0b544594064dd3eda8dba"
SRC_URI[sha256sum] = "0228c0364a7f2356fd7e7f1549937cbf3099a78d3b2eb1ba5bb0c31e2b89de7a"
SRC_URI += "\
    file://lua.patch \
"
