
# dropbearconvert is a tool to convert between Dropbear and OpenSSH
# private key formats. Drop it to save space.

SBINCOMMANDS_remove = "dropbearconvert"

# Normally the ssh command (ie used to initiate outgoing connections
# from the target) is not needed and can be removed to save space.
# However it may be used in multi-CPU systems for communication
# between two CPUs, so leave enabled by default.

# BINCOMMANDS_remove = "ssh"

do_configure_prepend() {

	# Drop older algorithms ( may duplicate changes made by dropbear-disable-weak-ciphers.patch so may already be applied )

	sed -e 's/#define DROPBEAR_ENABLE_CBC_MODE 1/#define DROPBEAR_ENABLE_CBC_MODE 0/' \
	    -e 's/#define DROPBEAR_SHA1_96_HMAC 1/#define DROPBEAR_SHA1_96_HMAC 0/' \
	    -e 's/#define DROPBEAR_DH_GROUP14_SHA1 1/#define DROPBEAR_DH_GROUP14_SHA1 0/' \
	    -e 's/#define DROPBEAR_DH_GROUP1 1/#define DROPBEAR_DH_GROUP1 0/' \
	    -i ${S}/default_options.h

	# Drop other older algorithms ( based on results from https://github.com/jtesta/ssh-audit )

	sed -e 's/#define DROPBEAR_ECDH 1/#define DROPBEAR_ECDH 0/' \
	    -i ${S}/default_options.h

	# Drop other stuff to reduce binary size ( may limit interoperability ? )

	sed -e 's/#define DROPBEAR_X11FWD 1/#define DROPBEAR_X11FWD 0/' \
	    -e 's/#define DROPBEAR_3DES 1/#define DROPBEAR_3DES 0/' \
	    -e 's/#define DROPBEAR_DSS 1/#define DROPBEAR_DSS 0/' \
	    -e 's/#define DROPBEAR_ECDSA 1/#define DROPBEAR_ECDSA 0/' \
	    -i ${S}/default_options.h
}

