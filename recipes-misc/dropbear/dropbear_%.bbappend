
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://dropbear_rsa_host_key"

do_install_append() {

	# There are various issues and solutions for SSH host keys in embedded
	# systems. Using a fixed key is efficient and simple (avoids needing to
	# include RSA keygen utils on the target, etc) and fine for development.

	install -d ${D}${sysconfdir}/dropbear
	install -m 0644 ${WORKDIR}/dropbear_rsa_host_key ${D}${sysconfdir}/dropbear/
}
