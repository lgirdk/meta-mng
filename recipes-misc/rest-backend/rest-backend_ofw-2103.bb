SUMMARY = "Server side javascript REST API implementation"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "f9cb5bfb3b142549ae9279b2f819694f"
SRC_URI[sha256sum] = "cae9891c4517a22fcaf46b26afc320446113e80fda532bb7a85e9243edb3cf11"

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
