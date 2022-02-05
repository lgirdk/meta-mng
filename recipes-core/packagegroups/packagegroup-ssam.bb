SUMMARY = "SSAM dependencies"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "packagegroup-ssam-build-deps packagegroup-ssam"

# Build dependencies

RDEPENDS_packagegroup-ssam-build-deps = " \
    libcrypto \
    libnl \
    libssl \
    ipset \
"

# Build + run time dependencies

RDEPENDS_packagegroup-ssam = " \
    ccsp-ssam-agent \
    packagegroup-ssam-build-deps \
"
