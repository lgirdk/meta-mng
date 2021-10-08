SUMMARY = "SK dependencies"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "packagegroup-sk-build-deps packagegroup-sk-build-deps-minimal packagegroup-sk"

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
    miniupnpc \
    nghttp2 \
    zlib \
"

# Build dependencies if SK delivers OSS as prebuilt binaries

RDEPENDS_packagegroup-sk-build-deps-minimal = " \
    libstdc++ \
    zlib \
"

# Build + run time dependencies

RDEPENDS_packagegroup-sk = " \
    curl \
    jq \
    killall \
    skta \
    packagegroup-sk-build-deps \
"

# This will be required eventually, but for now exclude it to avoid build
# issues with OE 2.2 (where the util-linux recipe in oe-core doesn't provide
# a separate package for util-linux-flock).
#
# RDEPENDS_packagegroup-sk += "util-linux-flock"
#
