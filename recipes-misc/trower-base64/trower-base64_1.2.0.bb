SUMMARY = "C implementation of base64 encode/decode"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSES/Apache-2.0.txt;md5=c846ebb396f8b174b10ded4771514fcc"

PV .= "+git${SRCPV}"

SRCREV = "0b3b30ee4790ea44e1318d05ceecd7297d40e7f7"

SRC_URI = "git://github.com/xmidt-org/trower-base64.git;branch=main \
           file://0001-create-versioned-shared-library.patch \
"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE += "-DBUILD_TESTING=OFF"
