
dirs755_append = " /rdklogs /minidumps /nvram /nvram2 /telemetry"

do_install_append() {

	rm -rf ${D}${sysconfdir}/skel

	rmdir ${D}/boot
	rmdir ${D}/media
}
