SUMMARY = "A lightweight SSH and SCP implementation"
HOMEPAGE = "http://matt.ucc.asn.au/dropbear/dropbear.html"
SECTION = "console/network"

# some files are from other projects and have others license terms:
#   public domain, OpenSSH 3.5p1, OpenSSH3.6.1p2, PuTTY
LICENSE = "MIT & BSD-3-Clause & BSD-2-Clause & PD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=25cf44512b7bc8966a48b6b1a9b7605f"

DEPENDS = "zlib virtual/crypt"
RPROVIDES_${PN} = "ssh sshd"
RCONFLICTS_${PN} = "openssh-sshd openssh"

SRC_URI = "http://matt.ucc.asn.au/dropbear/releases/dropbear-${PV}.tar.bz2 \
           file://0001-urandom-xauth-changes-to-options.h.patch \
           file://init \
           file://dropbear.default \
           ${@bb.utils.contains('PACKAGECONFIG', 'disable-weak-ciphers', 'file://dropbear-disable-weak-ciphers.patch', '', d)} "

SRC_URI[md5sum] = "a07438a6159a24c61f98f1bce2d479c0"
SRC_URI[sha256sum] = "48235d10b37775dbda59341ac0c4b239b82ad6318c31568b985730c788aac53b"

inherit autotools update-rc.d

CVE_PRODUCT = "dropbear_ssh"

INITSCRIPT_NAME = "dropbear"
INITSCRIPT_PARAMS = "defaults 10"

SBINCOMMANDS = "dropbear dropbearkey dropbearconvert"
BINCOMMANDS = "dbclient ssh scp"
EXTRA_OEMAKE = 'MULTI=1 SCPPROGRESS=1 PROGRAMS="${SBINCOMMANDS} ${BINCOMMANDS}"'

PACKAGECONFIG ?= "disable-weak-ciphers"
PACKAGECONFIG[system-libtom] = "--disable-bundled-libtom,--enable-bundled-libtom,libtommath libtomcrypt"
PACKAGECONFIG[disable-weak-ciphers] = ""

EXTRA_OECONF += "--disable-pam"

# This option appends to CFLAGS and LDFLAGS from OE
# This is causing [textrel] QA warning
EXTRA_OECONF += "--disable-harden"

# musl does not implement wtmp/logwtmp APIs
EXTRA_OECONF_append_libc-musl = " --disable-wtmp --disable-lastlog"

do_install() {
	install -d ${D}${sysconfdir} \
		${D}${sysconfdir}/init.d \
		${D}${sysconfdir}/default \
		${D}${sysconfdir}/dropbear \
		${D}${bindir} \
		${D}${sbindir} \
		${D}${localstatedir}

	install -m 0644 ${WORKDIR}/dropbear.default ${D}${sysconfdir}/default/dropbear

	install -m 0755 dropbearmulti ${D}${sbindir}/

	for i in ${BINCOMMANDS}
	do
		# ssh and scp symlinks are created by update-alternatives
		if [ $i = ssh ] || [ $i = scp ]; then continue; fi
		ln -s ${sbindir}/dropbearmulti ${D}${bindir}/$i
	done
	for i in ${SBINCOMMANDS}
	do
		ln -s ./dropbearmulti ${D}${sbindir}/$i
	done
	sed -e 's,/etc,${sysconfdir},g' \
		-e 's,/usr/sbin,${sbindir},g' \
		-e 's,/var,${localstatedir},g' \
		-e 's,/usr/bin,${bindir},g' \
		-e 's,/usr,${prefix},g' ${WORKDIR}/init > ${D}${sysconfdir}/init.d/dropbear
	chmod 755 ${D}${sysconfdir}/init.d/dropbear
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "20"
ALTERNATIVE_${PN} = "${@bb.utils.contains('BINCOMMANDS', 'scp', 'scp', '', d)} \
                     ${@bb.utils.contains('BINCOMMANDS', 'ssh', 'ssh', '', d)}"

ALTERNATIVE_TARGET = "${sbindir}/dropbearmulti"

pkg_postrm_append_${PN} () {
  if [ -f "${sysconfdir}/dropbear/dropbear_rsa_host_key" ]; then
        rm ${sysconfdir}/dropbear/dropbear_rsa_host_key
  fi
  if [ -f "${sysconfdir}/dropbear/dropbear_dss_host_key" ]; then
        rm ${sysconfdir}/dropbear/dropbear_dss_host_key
  fi
}

FILES_${PN} += "${bindir}"
