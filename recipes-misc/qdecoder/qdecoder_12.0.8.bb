SUMMARY = "A fastcgi compatible CGI library"
DESCRIPTION = "A fastcgi compatible CGI library that supports POST parameter parsing, sessions, cookies, and file downloads."
HOMEPAGE = "https://wolkykim.github.io/qdecoder/"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=64124cb9ced951d76f00106677aeb6cc"

PV .= "+git${SRCPV}"

SRCREV = "9d6ab323bd35f65060a47f4459e382bc6a9a8e4b"

SRC_URI = "git://github.com/wolkykim/qdecoder.git;protocol=https;branch=main \
           file://0001-Removed-generated-files-and-updated-to-latest-autoco.patch \
           file://0001-fix-configuring-with-enable-fastcgi-no.patch \
           file://0001-qdecoder-fixes-crash-when-multipart-boundary-is-miss.patch \
"

S = "${WORKDIR}/git"

inherit autotools

PACKAGECONFIG ?= "${@bb.utils.contains('DISTRO_FEATURES', 'fcgi', 'fcgi', '', d)}"

PACKAGECONFIG[fcgi] = "--enable-fastcgi=${STAGING_INCDIR},--disable-fastcgi,fcgi"
