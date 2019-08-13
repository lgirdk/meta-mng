
# Experimental hacks: allow bind to build with DSA and EXDSA support disabled in Openssl

EXTRA_OECONF_remove = "--with-ecdsa=yes"
EXTRA_OECONF_append = " --with-ecdsa=no"

CFLAGS_append = " -DPK11_DSA_DISABLE"
