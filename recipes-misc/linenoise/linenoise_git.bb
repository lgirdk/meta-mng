SUMMARY = "A minimal, zero-config, BSD licensed, readline replacement"
HOMEPAGE = "https://github.com/antirez/linenoise"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=faa55ac8cbebebcb4a84fe1ea2879578"

PV = "1.0+git${SRCPV}"

SRCREV = "97d2850af13c339369093b78abe5265845d78220"

SRC_URI = "git://github.com/antirez/linenoise.git;protocol=https"

S = "${WORKDIR}/git"

CLEANBROKEN = "1"

do_compile() {
	${CC} ${CFLAGS} -ffunction-sections -fdata-sections -I${S} -c linenoise.c -o linenoise.o
	${AR} rcs liblinenoise.a linenoise.o

	${CC} ${CFLAGS} -fPIC -I${S} -c linenoise.c -o linenoise_pic.o
	${CC} ${LDFLAGS} -shared -Wl,-soname,liblinenoise.so.0 linenoise_pic.o -o liblinenoise.so.0
}

do_install () {
	install -d ${D}${libdir}
	install -m 0644 liblinenoise.a ${D}${libdir}/

	# --------------------------------------------------------------------
	# Not installing the shared lib forces apps etc linking with
	# -llinenoise to use the static lib instead.
	# --------------------------------------------------------------------

#	install -m 0644 liblinenoise.so.0 ${D}${libdir}/
#	ln -s liblinenoise.so.0 ${D}${libdir}/liblinenoise.so

	install -d ${D}${includedir}
	install -m 0644 linenoise.h ${D}${includedir}/
}

RDEPENDS_${PN}-dev += "${PN}-staticdev"
