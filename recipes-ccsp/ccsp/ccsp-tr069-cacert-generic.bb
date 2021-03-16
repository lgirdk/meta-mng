SUMMARY = "CCSP Tr069 Root Certificate to verify ACS"
LICENSE = "CLOSED"

# This ccsp-tr069-cacert-generic recipe can be used as a placeholder to satisfy
# a runtime dependency on ccsp-tr069-cacert. It provides the generic GlobalSign
# R1 Root Certificate. A private ACS is more likely to identify itself with a
# certificate signed with a proprietary Root Certificate so in production
# builds the run time dependency ccsp-tr069-cacert is expected to be satisfied
# by an alternative package.

SRC_URI = "file://cacert.pem"

inherit allarch

do_install() {
	install -d ${D}/etc
	install -m 644 ${WORKDIR}/cacert.pem ${D}/etc/
}

RPROVIDES_${PN} = "ccsp-tr069-cacert"
