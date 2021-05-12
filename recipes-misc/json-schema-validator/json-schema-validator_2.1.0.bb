SUMMARY = "JSON schema validator for JSON for Modern C++"
HOMEPAGE = "https://github.com/pboettch/json-schema-validator"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c441d022da1b1663c70181a32225d006"

DEPENDS = "nlohmann-json"

PV .= "+git${SRCPV}"

SRC_URI = "git://github.com/pboettch/json-schema-validator.git"

SRCREV = "27fc1d094503623dfe39365ba82581507524545c"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE = "-DBUILD_SHARED_LIBS=ON"

PACKAGES =+ "${PN}-utils"

FILES_${PN}-utils = "${bindir}"
