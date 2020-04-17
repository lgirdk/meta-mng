
DEPLOY_DIR_PREBUILT_RECIPES ?= "${DEPLOY_DIR}/prebuilt-recipes"
DEPLOY_DIR_PREBUILT_TARFILES ?= "${DEPLOY_DIR}/prebuilt-tarfiles"

BASEPV = "${@ d.getVar('PV', True).replace('AUTOINC+', '')}"

PREBUILT_PV ?= "prebuilt-${MACHINE}-${BASEPV}"
PREBUILT_TARFILE_BASENAME ?= "${BPN}-${PREBUILT_PV}"

PREBUILT_RECIPE ?= "${DEPLOY_DIR_PREBUILT_RECIPES}/${BPN}_prebuilt-${MACHINE}.bb"
PREBUILT_TARFILE ?= "${DEPLOY_DIR_PREBUILT_TARFILES}/${PREBUILT_TARFILE_BASENAME}.tar.bz2"

PREBUILT_SRC_URI_PREFIX ?= "http://localhost:8000/"
PREBUILT_SRC_URI_PATH ?= ""

PREBUILT_CREATE_TARFILE_DOTDONE ?= "true"

do_genprebuilt[depends] += "pbzip2-native:do_populate_sysroot"
do_genprebuilt[dirs] = "${DEPLOY_DIR_PREBUILT_RECIPES} ${DEPLOY_DIR_PREBUILT_TARFILES} ${WORKDIR}"

do_genprebuilt() {

    [ "${CLASSOVERRIDE}" != "class-target" ] && exit 0

    find package -type d -empty -o ! -type d | grep -v '^package/usr/src' | grep -v '\.debug' | LC_ALL=C sort > ${PREBUILT_TARFILE}.files
    tar --transform "s|^package|${PREBUILT_TARFILE_BASENAME}|" --owner 0 --group 0 -T ${PREBUILT_TARFILE}.files -cvf - | pbzip2 > ${PREBUILT_TARFILE}
    rm ${PREBUILT_TARFILE}.files

    [ "${PREBUILT_CREATE_TARFILE_DOTDONE}" = "true" ] && touch ${PREBUILT_TARFILE}.done

    # ------------------------------------------------------------------------

    s='$''{S}'
    d='$''{D}'
    bp='$''{BP}'
    ma='$''{MACHINE_ARCH}'
    pn='$''{PN}'
    wd='$''{WORKDIR}'
    openbracket='{'
    closebracket='}'

    md5=`md5sum < ${PREBUILT_TARFILE} | cut -c-32`
    sha256=`sha256sum < ${PREBUILT_TARFILE} | cut -c-64`

    # ------------------------------------------------------------------------

    cat << EOF > ${PREBUILT_RECIPE}

COMPATIBLE_MACHINE = "${MACHINE}"

PV = "${PREBUILT_PV}"

SRC_URI = "${PREBUILT_SRC_URI_PREFIX}${PREBUILT_SRC_URI_PATH}$bp.tar.bz2"

SRC_URI[md5sum] = "$md5"
SRC_URI[sha256sum] = "$sha256"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () $openbracket
    tar -C $s --exclude='./patches' --exclude='./.pc' -cpf - . | tar -C $d --no-same-owner -xpvf -
$closebracket

PACKAGE_ARCH = "$ma"

INSANE_SKIP_$pn += "already-stripped"
EOF

    # ------------------------------------------------------------------------
}

addtask do_genprebuilt after do_package before do_package_qa
