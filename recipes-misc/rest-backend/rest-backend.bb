SUMMARY = "Server side javascript REST API implementation"
LICENSE = "CLOSED"

PV .= "+git${SRCPV}"

SRC_URI = "${LGI_UI_GIT}/${BPN}${LGI_UI_GIT_SUFFIX};protocol=${LGI_UI_GIT_PROTOCOL}${LGI_UI_GIT_EXTRAOPT}"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

MIRRORS = "${MIRRORS_PRIVATE}"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake DESTDIR='${D}' install

    # Move front.jse to a servable location
    install -d ${D}/www/common/rest
    mv ${D}/www/jse/framework/front.jse ${D}/www/common/rest

    # Move saverestore.jse to a servable location
    install -d ${D}/www/pages/saverestore
    mv ${D}/www/jse/saverestore/saverestore.jse ${D}/www/pages/saverestore
}

FILES_${PN} += "/www"
