SUMMARY = "Web User Interface"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "b4f53d46b4e42296ca9c39288fa7f8b6"
SRC_URI[sha256sum] = "c6fa25e412b1770a12961bfe78fe15d35d6508ac687802783a69938059357ac4"

MIRRORS = "${MIRRORS_PRIVATE}"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake DESTDIR='${D}' install
}

FILES_${PN} += "/www"

RDEPENDS_${PN} += "rest-backend"
