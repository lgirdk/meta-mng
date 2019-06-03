DESCRIPTION = "Tiny XML Library"
HOMEPAGE = "https://www.msweet.org/mxml/"
BUGTRACKER = "https://github.com/michaelrsweet/mxml/issues"
LICENSE = "Mini-XML-License"
LIC_FILES_CHKSUM = "file://COPYING;md5=a6ba38606d63bb042c5d8cfee182e120"

SRCREV = "3aaa12c7d709d05286255d191998f29105dd407a"

SRC_URI = "git://github.com/michaelrsweet/mxml.git"

S = "${WORKDIR}/git"

inherit lib_package

# Snapshot of CONFIGUREOPTS extract from the autotools class (using the
# autotools class directly doesn't work)

CONFIGUREOPTS = " --build=${BUILD_SYS} \
                  --host=${HOST_SYS} \
                  --prefix=${prefix} \
                  --exec_prefix=${exec_prefix} \
                  --bindir=${bindir} \
                  --sbindir=${sbindir} \
                  --libexecdir=${libexecdir} \
                  --datadir=${datadir} \
                  --sysconfdir=${sysconfdir} \
                  --sharedstatedir=${sharedstatedir} \
                  --localstatedir=${localstatedir} \
                  --libdir=${libdir} \
                  --includedir=${includedir} \
                  --oldincludedir=${oldincludedir} \
                  --infodir=${infodir} \
                  --mandir=${mandir} \
"

do_configure() {
	./configure ${CONFIGUREOPTS} --enable-shared
}

do_install () {
	oe_runmake DSTROOT=${D} install
}
