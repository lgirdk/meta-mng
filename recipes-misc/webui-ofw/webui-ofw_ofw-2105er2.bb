SUMMARY = "Web User Interface"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "29e82abd3566f47aa07d8082e1b5b958"
SRC_URI[sha256sum] = "9329008eac7bcad3025d696d24dff5015d4fb308a1a2b9c277f7b95e5fe1a79b"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake DESTDIR='${D}' install
}

FILES_${PN} += "/www"

RDEPENDS_${PN} += "rest-backend"
