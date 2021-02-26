SUMMARY = "Web User Interface"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "b3db53a03d84c130a3d20a3654b85ddc"
SRC_URI[sha256sum] = "69afca6b31e1033975a21e75f9890e0d4a39b8247a4a049b88ceb76594091551"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake DESTDIR='${D}' install
}

FILES_${PN} += "/www"

RDEPENDS_${PN} += "rest-backend"
