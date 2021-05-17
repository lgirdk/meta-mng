SUMMARY = "Web User Interface"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "c7045e0e7cdd571f13984abc45afd887"
SRC_URI[sha256sum] = "2d01602d3ec3bdd65e03e24ec689c0e09e32fd604098c270812c48916908d65f"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake DESTDIR='${D}' install
}

FILES_${PN} += "/www"

RDEPENDS_${PN} += "rest-backend"
