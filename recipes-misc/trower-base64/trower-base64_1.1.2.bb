SUMMARY = "C implementation of base64 encode/decode"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b1e01b26bacfc2232046c90a330332b3"

PV .= "+git${SRCPV}"

SRCREV = "f1364804df1282526816752529a54dbe99d43f10"

SRC_URI = "git://github.com/xmidt-org/trower-base64.git"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE += "-DBUILD_TESTING=OFF"
