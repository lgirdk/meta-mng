SUMMARY = "A simple RADIUS client library"
HOMEPAGE = "https://radcli.github.io/radcli/"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=98f20548b0e2bc66e5583e541fb647bd"

SRC_URI = "https://github.com/radcli/radcli/releases/download/${PV}/${BP}.tar.gz"

SRC_URI[md5sum] = "385d9c801c808abdd59880f8a9de0d18"
SRC_URI[sha256sum] = "20ddc8429d5912dfa2e71fafc93881844ce98e898c041b1dd7f757b9ddc8fcfd"

SRC_URI += " \
	file://rfc-congestive-back-off.patch \
"

inherit autotools pkgconfig

PACKAGECONFIG ?= ""

PACKAGECONFIG[tls] = "--with-tls,--without-tls,gnutls"
PACKAGECONFIG[nettle] = "--with-nettle,--without-nettle,nettle"

EXTRA_OECONF = "--disable-rpath"
