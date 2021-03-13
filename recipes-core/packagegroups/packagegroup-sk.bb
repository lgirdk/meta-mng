SUMMARY = "SK dependencies"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "packagegroup-sk-build-deps packagegroup-sk"

# Build dependencies

RDEPENDS_packagegroup-sk-build-deps = " \
    jansson \
    libavcodec \
    libavformat \
    libavutil \
    libcrypto \
    libcurl \
    libpcap \
    libssl \
    libstdc++ \
    nghttp2 \
"

# Build + run time dependencies

RDEPENDS_packagegroup-sk = " \
    curl \
    jq \
    killall \
    util-linux-flock \
    packagegroup-sk-build-deps \
"
