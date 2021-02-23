
SRC_URI_append = " \
    file://ui2-cgi.conf \
    file://ui2-fast-cgi.conf \
"

INHIBIT_UPDATERCD_BBCLASS = "1"

SYSTEMD_AUTO_ENABLE = "disable"

GWCONF = "${@bb.utils.contains('DISTRO_FEATURES', 'fcgi', 'ui2-fast-cgi.conf', 'ui2-cgi.conf', d)}"

do_install_append () {
	install -m 644 ${WORKDIR}/${GWCONF} ${D}${sysconfdir}/lighttpd/gateway.conf
}

# Move the example /www provided by lighttpd into a separate package so that
# it can be easily excluded from images where /www is provided by another
# package.

PACKAGES =+ "${PN}-www"

FILES_${PN}-www = "/www"

RRECOMMENDS_${PN} += "${PN}-www"

# Extra modules etc required for webui

RRECOMMENDS_${PN} += " \
    ccsp-jse \
    lighttpd-module-alias \
    lighttpd-module-rewrite \
    lighttpd-module-setenv \
    ${@bb.utils.contains('DISTRO_FEATURES', 'fcgi', 'lighttpd-module-fastcgi', 'lighttpd-module-cgi', d)} \
"
