
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI_append = " \
    file://ui2-cgi.conf \
    file://ui2-fast-cgi.conf \
"

INHIBIT_UPDATERCD_BBCLASS = "1"

SYSTEMD_AUTO_ENABLE = "disable"

GWCONF = "${@bb.utils.contains('DISTRO_FEATURES', 'fcgi', 'ui2-fast-cgi.conf', 'ui2-cgi.conf', d)}"

do_install_append () {

	# Insert contents of gateway.conf into lighttpd.conf
	# ie make lighttpd.conf self contained so that a modified version
	# created at run time in (e.g.) /tmp doesn't need a copy of
	# gateway.conf available in /tmp too (since 'include xxx' paths are
	# relative to the path containing lighttpd.conf).

	sed -e '/include "gateway.conf"/r ${WORKDIR}/${GWCONF}' \
	    -e '/include "gateway.conf"/d' \
	    -i ${D}${sysconfdir}/lighttpd/lighttpd.conf

	# Strip comments and squash empty lines from lighttpd.conf

	for f in lighttpd.conf
	do
		sed 's/[ \t]\+$//; /^#/d' ${D}${sysconfdir}/lighttpd/$f | cat --squeeze-blank > ${D}${sysconfdir}/lighttpd/xxx_$f
		mv ${D}${sysconfdir}/lighttpd/xxx_$f ${D}${sysconfdir}/lighttpd/$f
	done
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
    lighttpd-module-redirect \
    lighttpd-module-rewrite \
    lighttpd-module-setenv \
    ${@bb.utils.contains('DISTRO_FEATURES', 'fcgi', 'lighttpd-module-fastcgi', 'lighttpd-module-cgi', d)} \
"
