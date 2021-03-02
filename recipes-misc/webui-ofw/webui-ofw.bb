SUMMARY = "Web User Interface"
LICENSE = "CLOSED"

PV .= "+git${SRCPV}"

SRC_URI = "${LGI_UI_GIT}/${WEBUI_OFW_REPONAME}${LGI_UI_GIT_SUFFIX};protocol=${LGI_UI_GIT_PROTOCOL}${LGI_UI_GIT_EXTRAOPT}"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

MIRRORS = "${MIRRORS_PRIVATE}"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake DESTDIR='${D}' install
}

FILES_${PN} += "/www"

RDEPENDS_${PN} += "rest-backend"
