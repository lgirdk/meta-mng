SUMMARY = "Web User Interface"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "f81d3abaa5299a485be38960087ab00c"
SRC_URI[sha256sum] = "92ed16c7d15fd2a4de7f95040c22cb5af2ed91109a7a3c47207d69add790d0cb"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake DESTDIR='${D}' install
}

FILES_${PN} += "/www"

RDEPENDS_${PN} += "rest-backend"
