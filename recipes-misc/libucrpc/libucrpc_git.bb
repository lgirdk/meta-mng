SUMMARY = "Sun RPC library extracted from uClibc-ng"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://include/rpc/rpc.h;beginline=2;endline=29;md5=271ab487a9a5aebb9c567d6453d1e72c"

PV = "0.1+git${SRCPV}"

SRCREV = "1b6360e854ee76f37fb2a1c89c3150545f95c25c"

SRC_URI = "git://github.com/armcc/${BPN}.git;protocol=https"

S = "${WORKDIR}/git"

inherit autotools
