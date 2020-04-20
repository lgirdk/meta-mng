require dnsmasq.inc

SRC_URI[dnsmasq-2.81.md5sum] = "2642c3f17f4a9762a83dd70daa6cfeb6"
SRC_URI[dnsmasq-2.81.sha256sum] = "749ca903537c5197c26444ac24b0dce242cf42595fdfe6b9a5b9e4c7ad32f8fb"
SRC_URI += "\
    file://lua.patch \
"
