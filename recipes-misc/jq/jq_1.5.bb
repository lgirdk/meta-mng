SUMMARY = "Lightweight and flexible command-line JSON processor"
DESCRIPTION = "jq is like sed for JSON data, you can use it to slice and \
               filter and map and transform structured data with the same \
               ease that sed, awk, grep and friends let you play with text."
HOMEPAGE = "https://stedolan.github.io/jq/"
BUGTRACKER = "https://github.com/stedolan/jq/issues"
SECTION = "utils"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=29dd0c35d7e391bb8d515eacf7592e00"

SRC_URI = "https://github.com/stedolan/${BPN}/releases/download/${BP}/${BP}.tar.gz \
           file://Support-without-oniguruma.patch \
"

SRC_URI[md5sum] = "0933532b086bd8b6a41c1b162b1731f9"
SRC_URI[sha256sum] = "c4d2bfec6436341113419debf479d833692cc5cdab7eb0326b5a4d4fbe9f493c"

inherit autotools-brokensep

PACKAGECONFIG ?= ""

PACKAGECONFIG[docs] = "--enable-docs,--disable-docs,ruby-native"
PACKAGECONFIG[oniguruma] = "--with-oniguruma,--without-oniguruma,onig"

OE_EXTRACONF += " \
    --disable-maintainer-mode \
    --disable-valgrind \
"

# We don't need a separate libjq.so for other apps to link with, so
# save space by forcing the jq binary to link statically with libjq.

EXTRA_OECONF += " \
    --disable-shared --enable-static \
"

CFLAGS += "-ffunction-sections -fdata-sections"
LDFLAGS += "-Wl,--gc-sections"

BBCLASSEXTEND = "native"
