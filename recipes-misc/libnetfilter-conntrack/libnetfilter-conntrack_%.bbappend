
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-fix-address-family-data-type-for-nfct_query-in-connt.patch"

do_compile_append() {
	# Build examples ( see https://git.netfilter.org/libnetfilter_conntrack/tree/README )
	oe_runmake check
}

do_install_append() {
	# Install (a subset of) the examples
	install -d ${D}${sbindir}
	install -m 755 ${B}/utils/.libs/conntrack_flush ${D}${sbindir}/
	install -m 755 ${B}/utils/.libs/expect_flush ${D}${sbindir}/
}
