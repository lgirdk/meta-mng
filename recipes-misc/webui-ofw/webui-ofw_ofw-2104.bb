SUMMARY = "Web User Interface"
LICENSE = "CLOSED"

SRC_URI = "http://localhost:8000/${BP}.tgz"

SRC_URI[md5sum] = "2d8a902b1386e2c6f0ceb4d0c7c5b600"
SRC_URI[sha256sum] = "dc50d4973cbf38e614c80151e3a4830b1b67aa5a85fac45af927153e1a6a9896"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake DESTDIR='${D}' install
}

FILES_${PN} += "/www"

RDEPENDS_${PN} += "rest-backend"
