
PACKAGECONFIG_remove_class-target = "gnutls libidn verbose"
PACKAGECONFIG_append_class-target = " ssl"

EXTRA_OECONF += " \
    --without-ca-bundle \
    --without-ca-path \
    --without-ca-fallback \
"

RRECOMMENDS_lib${BPN}_remove = "ca-certificates"
