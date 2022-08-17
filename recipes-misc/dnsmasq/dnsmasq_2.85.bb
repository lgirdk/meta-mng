require dnsmasq.inc

SRC_URI[md5sum] = "f32403e94a657b93d7fbe0a9c07ebccf"
SRC_URI[sha256sum] = "ad98d3803df687e5b938080f3d25c628fe41c878752d03fbc6199787fee312fa"
SRC_URI += "\
    file://lua.patch \
"
