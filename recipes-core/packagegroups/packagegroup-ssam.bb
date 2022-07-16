SUMMARY = "SSAM dependencies"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "packagegroup-ssam-build-deps packagegroup-ssam"

# Build dependencies
# Note: libnl is not included here as SSAM provide their own version.

RDEPENDS_packagegroup-ssam-build-deps = " \
    libcrypto \
    libssl \
    ipset \
"

# Build + run time dependencies

RDEPENDS_packagegroup-ssam = " \
    packagegroup-ssam-build-deps \
"
