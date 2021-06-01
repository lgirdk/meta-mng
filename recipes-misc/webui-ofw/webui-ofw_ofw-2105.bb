SUMMARY = "Web User Interface"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "7d5552c51016e8f4c6fbdc95044269d0"
SRC_URI[sha256sum] = "ff771d31d8bcba440521ff7bac0651bfeaec80b152140bb152b40c75ba1e6753"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake DESTDIR='${D}' install
}

FILES_${PN} += "/www"

RDEPENDS_${PN} += "rest-backend"
