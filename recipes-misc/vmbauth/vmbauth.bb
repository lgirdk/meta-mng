SUMMARY = "VMB Utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://vmbauth.c;endline=15;md5=0a1cd7ecdd534dda17fd74ff0e5e19e5"

DEPENDS = "radcli utopia"

SRC_URI = "file://vmbauth.c;subdir=${BP} \
           file://vmbping.c;subdir=${BP} \
           file://vmb-dhcpc.sh \
           file://vmb-mode.sh \
           file://vmbauth.sh \
"

do_compile() {
	${CC} ${CFLAGS} -ffunction-sections -fdata-sections -c vmbauth.c -o vmbauth.o
	${CC} ${CFLAGS} ${LDFLAGS} -Wl,--gc-sections vmbauth.o -o vmbauth -lradcli -lsyscfg

	${CC} ${CFLAGS} -ffunction-sections -fdata-sections -c vmbping.c -o vmbping.o
	${CC} ${CFLAGS} ${LDFLAGS} -Wl,--gc-sections vmbping.o -o vmbping
}

do_install() {
	install -d ${D}${bindir}
	install -m 755 ${B}/vmbauth ${D}${bindir}/
	install -m 755 ${B}/vmbping ${D}${bindir}/
	install -m 755 ${WORKDIR}/vmb-mode.sh ${D}${bindir}/
	install -m 755 ${WORKDIR}/vmbauth.sh ${D}${bindir}/

	install -d ${D}${sysconfdir}
	install -m 755 ${WORKDIR}/vmb-dhcpc.sh ${D}${sysconfdir}/
}
