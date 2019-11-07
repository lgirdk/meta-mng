
INHIBIT_UPDATERCD_BBCLASS = "1"

SYSTEMD_AUTO_ENABLE = "disable"

# Move the example /www provided by lighttpd into a separate package so that
# it can be easily excluded from images where /www is provided by another
# package.

PACKAGES =+ "${PN}-www"

FILES_${PN}-www = "/www"

RRECOMMENDS_${PN} += "${PN}-www"
