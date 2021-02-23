
PACKAGECONFIG_remove_class-target = "gnutls libidn verbose"

PACKAGECONFIG_append_class-target = " ssl"
PACKAGECONFIG_append_class-target = " ${@bb.utils.contains('DISTRO_FEATURES', 'sk', 'nghttp2', '', d)}"

EXTRA_OECONF += " \
    --without-ca-bundle \
    --without-ca-path \
    --without-ca-fallback \
"

RRECOMMENDS_lib${BPN}_remove = "ca-certificates"
