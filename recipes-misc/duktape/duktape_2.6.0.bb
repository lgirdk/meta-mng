SUMMARY = "Duktape is an embeddable Javascript engine, with a focus on portability and compact footprint"
HOMEPAGE = "https://duktape.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=c83446610de1f63c7ca60cfcc82dec9d"

SRC_URI = "https://duktape.org/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "01ee8ecf3dd5c6504543c8679661bb20"
SRC_URI[sha256sum] = "96f4a05a6c84590e53b18c59bb776aaba80a205afbbd92b82be609ba7fe75fa7"

CLEANBROKEN = "1"

do_compile() {
	${CC} ${CFLAGS} -ffunction-sections -fdata-sections -I${S}/src -c src/duktape.c -o src/duktape.o
	${CC} ${CFLAGS} -ffunction-sections -fdata-sections -I${S}/src -c extras/print-alert/duk_print_alert.c -o extras/print-alert/duk_print_alert.o
	${AR} rcs libduktape.a src/duktape.o extras/print-alert/duk_print_alert.o

#	${CC} ${CFLAGS} -I${S}/src -fPIC -c src/duktape.c -o src/duktape_pic.o
#	${CC} ${CFLAGS} -I${S}/src -fPIC -c extras/print-alert/duk_print_alert.c -o extras/print-alert/duk_print_alert_pic.o
#	${CC} ${LDFLAGS} -shared -Wl,-soname,libduktape.so.${PV} src/duktape_pic.o extras/print-alert/duk_print_alert_pic.o -o libduktape.so.${PV} -lm
}

do_compile_append_class-native() {

	# Build the command line tool. statically linked with libduktape.a
	# Note that there's no .so symlink to the shared lib in the build
	# directory, so dynamic linking won't work without further tweaks.

	${CC} ${CFLAGS} -DDUK_CMDLINE_PRINTALERT_SUPPORT -I${S}/src -I${S}/examples/cmdline -I${S}/extras/print-alert -L${B} -o duk examples/cmdline/duk_cmdline.c -Wl,--gc-sections -l:libduktape.a -lm
}

do_install () {
	install -d ${D}${libdir}
	install -m 0644 libduktape.a ${D}${libdir}/

	# --------------------------------------------------------------------
	# Not installing the shared lib forces apps etc linking with
	# -lduktake to use the static lib instead.
	# --------------------------------------------------------------------

#	install -m 0644 libduktape.so.${PV} ${D}${libdir}/
#	ln -s libduktape.so.${PV} ${D}${libdir}/libduktape.so

	install -d ${D}${includedir}/duktape
	install -m 0644 src/duktape.h ${D}${includedir}/duktape/
	install -m 0644 src/duk_config.h ${D}${includedir}/duktape/
	install -m 0644 extras/print-alert/duk_print_alert.h ${D}${includedir}/duktape/
}

do_install_class-native() {
	install -d ${D}${bindir}
	install -m 755 duk ${D}${bindir}/

	# No need to install the shared lib if duk binary is statically linked.
}

RDEPENDS_${PN}-dev += "${PN}-staticdev"

BBCLASSEXTEND = "native"
