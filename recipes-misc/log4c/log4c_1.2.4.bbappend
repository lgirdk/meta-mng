
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI_append = " file://disable-log4crc-version-check.patch"
