SUMMARY = "Server side javascript REST API implementation"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "161f65aae1e72cd99435846c4be9f77f"
SRC_URI[sha256sum] = "87d8bb44b69d36be646462962f482f85ef9c149e529ba8f2dae42b5538e5ad40"

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
