SUMMARY = "SSAM dependencies"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "packagegroup-ssam-build-deps packagegroup-ssam"

# Build dependencies

RDEPENDS_packagegroup-ssam-build-deps = " \
    libcrypto \
    libnl \
    libssl \
"

# Build + run time dependencies

RDEPENDS_packagegroup-ssam = " \
    packagegroup-ssam-build-deps \
"
