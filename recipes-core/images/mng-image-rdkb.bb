
require mng-image-base.bb

SUMMARY = "MNG RDKB Image"

IMAGE_INSTALL += " \
    ccsp-cm-agent \
    ccsp-common-library \
    ccsp-cr \
    ccsp-dmcli \
    ccsp-eth-agent \
    ccsp-home-security \
    ccsp-hotspot \
    ccsp-hotspot-kmod \
    ccsp-lm-lite \
    ccsp-logagent \
    ccsp-misc \
    ccsp-moca \
    ccsp-mta-agent \
    ccsp-p-and-m \
    ccsp-psm \
    ccsp-snmp-pa \
    ccsp-tr069-pa \
    ccsp-wifi-agent \
    ccsp-xdns \
    notify-comp \
    sysint-broadband \
    test-and-diagnostic \
    utopia \
"

IMAGE_INSTALL += " \
    dibbler-client \
    quagga \
    quagga-ripd \
    quagga-ripngd \
    lighttpd \
"

# ----------------------------------------------------------------------------

# The kernel module doesn't seem to build with new qemu kernels?
# Exclude it as temporary workaround.

IMAGE_INSTALL_remove_qemuall = "ccsp-hotspot-kmod"

# ----------------------------------------------------------------------------
