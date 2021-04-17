SUMMARY = "Server side javascript REST API implementation"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "a1e3536af8d1643771400378ad59980a"
SRC_URI[sha256sum] = "60f586a695c30ec4175433c8ba2607e08e19e07f4dc6cd0b48e6fc6578f97c40"

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
