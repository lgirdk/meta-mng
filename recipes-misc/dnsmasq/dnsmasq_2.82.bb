require dnsmasq.inc

SRC_URI[dnsmasq-2.82.md5sum] = "0003fe90b69463c1f687f4ae8560f13a"
SRC_URI[dnsmasq-2.82.sha256sum] = "84523646f3116bb5e1151efb66e645030f6e6a8256f29aab444777a343ebc132"
SRC_URI += "\
    file://lua.patch \
"

