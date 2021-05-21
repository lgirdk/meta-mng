
# Disable unnecessary features to save space.

EXTRA_OECONF += " \
    --disable-help-builtin \
"

do_install_append () {
	# Drop script to send email to the bash developers
	rm ${D}${bindir}/bashbug
	rmdir ${D}${bindir}
}
