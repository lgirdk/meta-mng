
# Disable weaker or unused crypto algorithms.

# Digests
#
EXTRA_OECONF_append_class-target = " \
    no-md2 \
    no-md4 \
    no-mdc2 \
    no-ripemd \
    no-whirlpool \
"

# Symetric Ciphers ( ** Skip "no-des" for now since it's required by ti_kerberos ** )
#
EXTRA_OECONF_append_class-target = " \
    no-bf \
    no-camellia \
    no-cast \
    no-idea \
    no-rc2 \
    no-rc4 \
    no-rc5 \
    no-seed \
"

# Public Key Ciphers
#
EXTRA_OECONF_append_class-target = " \
    no-dsa \
    no-ec \
    no-ecdh \
    no-ecdsa \
"

# Obsolete SSL versions
#
EXTRA_OECONF_append_class-target = " \
    no-ssl2 \
    no-ssl3 \
"

# Misc...
#
EXTRA_OECONF_append_class-target = " \
    no-engine \
    no-hw \
    no-gmp \
    no-sctp \
    no-srp \
    no-srtp \
    no-unit-test \
    no-zlib \
    no-zlib-dynamic \
"
