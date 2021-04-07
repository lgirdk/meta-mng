
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

# Add the webui and remove lighttpd-www (which is otherwise going to clash with
# it if both are included in the image).

# IMAGE_INSTALL += "webui-ofw"

BAD_RECOMMENDATIONS += "lighttpd-www"

# ----------------------------------------------------------------------------

IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'sk', 'packagegroup-sk', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'ssam', 'packagegroup-ssam', '', d)}"

# ----------------------------------------------------------------------------

# The kernel module doesn't seem to build with new qemu kernels?
# Exclude it as temporary workaround.

IMAGE_INSTALL_remove_qemuall = "ccsp-hotspot-kmod"

# ----------------------------------------------------------------------------
