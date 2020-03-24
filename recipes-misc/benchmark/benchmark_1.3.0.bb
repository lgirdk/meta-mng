DESCRIPTION = "A library to benchmark code snippets, similar to unit tests"
HOMEPAGE = "https://github.com/google/benchmark"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57" 

PV .= "+git${SRCPV}"

SRCREV = "336bb8db986cc52cdf0cefa0a7378b9567d1afee"

SRC_URI = "git://github.com/google/benchmark.git"

S = "${WORKDIR}/git"

inherit cmake
