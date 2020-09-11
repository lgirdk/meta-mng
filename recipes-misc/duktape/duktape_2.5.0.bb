SUMMARY = "Duktape is an embeddable Javascript engine, with a focus on portability and compact footprint"
HOMEPAGE = "https://duktape.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=c83446610de1f63c7ca60cfcc82dec9d"

SRC_URI = "https://duktape.org/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "e55fe3830f0d469dc67205b380515af0"
SRC_URI[sha256sum] = "83d411560a1cd36ea132bd81d8d9885efe9285c6bc6685c4b71e69a0c4329616"

CLEANBROKEN = "1"

do_compile() {
	${CC} ${CFLAGS} -ffunction-sections -fdata-sections -I${S}/src -c src/duktape.c -o src/duktape.o
	${CC} ${CFLAGS} -ffunction-sections -fdata-sections -I${S}/src -c extras/print-alert/duk_print_alert.c -o extras/print-alert/duk_print_alert.o
	${AR} rcs libduktape.a src/duktape.o extras/print-alert/duk_print_alert.o

	${CC} ${CFLAGS} -I${S}/src -fPIC -c src/duktape.c -o src/duktape_pic.o
	${CC} ${CFLAGS} -I${S}/src -fPIC -c extras/print-alert/duk_print_alert.c -o extras/print-alert/duk_print_alert_pic.o
	${CC} ${LDFLAGS} -shared -Wl,-soname,libduktape.so.${PV} src/duktape_pic.o extras/print-alert/duk_print_alert_pic.o -o libduktape.so.${PV} -lm
}

do_install () {
	install -d ${D}${libdir}
	install -m 0644 libduktape.a ${D}${libdir}/
	install -m 0644 libduktape.so.${PV} ${D}${libdir}/
	ln -s libduktape.so.${PV} ${D}${libdir}/libduktape.so

	install -d ${D}${includedir}/duktape
	install -m 0644 src/duktape.h ${D}${includedir}/duktape/
	install -m 0644 src/duk_config.h ${D}${includedir}/duktape/
	install -m 0644 extras/print-alert/duk_print_alert.h ${D}${includedir}/duktape/
}
