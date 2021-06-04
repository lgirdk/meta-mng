SUMMARY = "Server side javascript REST API implementation"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "adcfdb10632ee6bd15c9042d6ed77984"
SRC_URI[sha256sum] = "d89744746ac38c6e266826f0a6765823a81b5a898f003e6171ba351915194fae"

MIRRORS = "${MIRRORS_PRIVATE}"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake DESTDIR='${D}' install

    # Move front.jse to a servable location
    install -d ${D}/www/pages/rest
    mv ${D}/www/jse/framework/front.jse ${D}/www/pages/rest

    # Move saverestore.jse to a servable location
    install -d ${D}/www/pages/saverestore
    mv ${D}/www/jse/saverestore/saverestore.jse ${D}/www/pages/saverestore
}

FILES_${PN} += "/www"
