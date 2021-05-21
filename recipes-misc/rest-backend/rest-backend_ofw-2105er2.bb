SUMMARY = "Server side javascript REST API implementation"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "67c37b03995e0ed748fbe22b6c3c26b9"
SRC_URI[sha256sum] = "73d11e0d91db1bb2c6f214845ee74b63349ca415cd370436c85c5a674e3cea0e"

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
