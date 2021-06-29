SUMMARY = "SK Test App"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://skta.c;endline=15;md5=3624884a41910c90c0cc8e345c944066"

SRC_URI = "file://skta.c;subdir=${BP}"

do_compile() {
	${CC} ${CFLAGS} -ffunction-sections -fdata-sections -c skta.c -o skta.o
	${CC} ${CFLAGS} ${LDFLAGS} -Wl,--gc-sections skta.o -o skta
}

do_install () {
	install -d ${D}${bindir}
	install -m 755 ${B}/skta ${D}${bindir}/
}
