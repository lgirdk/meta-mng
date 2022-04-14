
require mng-image-base.bb

SUMMARY = "MNG RDKB Image"

IMAGE_INSTALL += " \
    ccsp-cm-agent \
    ccsp-common-library \
    ccsp-cr \
    ccsp-dmcli \
    ccsp-eth-agent \
    ccsp-hotspot \
    ccsp-hotspot-kmod \
    ccsp-lm-lite \
    ccsp-logagent \
    ccsp-misc \
    ${@bb.utils.contains('DISTRO_FEATURES', 'moca', 'ccsp-moca', '', d)} \
    ccsp-mta-agent \
    ccsp-p-and-m \
    ccsp-psm \
    ${@bb.utils.contains('DISTRO_FEATURES', 'snmppa', 'ccsp-snmp-pa', '', d)} \
    ccsp-tr069-pa \
    ccsp-wifi-agent \
    notify-comp \
    sysint-broadband \
    test-and-diagnostic \
    utopia \
"

IMAGE_INSTALL += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_gpon_manager', 'rdkgponmanager', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', 'rdk-fwupgrade-manager', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', 'rdk-vlanmanager', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', 'rdk-wanmanager', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', 'rdktelcovoicemanager', '', d)} \
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

IMAGE_INSTALL += "packagegroup-webui"

BAD_RECOMMENDATIONS += "lighttpd-www"

# ----------------------------------------------------------------------------

IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'sk', 'packagegroup-sk', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'ssam', 'packagegroup-ssam', '', d)}"

# ----------------------------------------------------------------------------

# The kernel module doesn't seem to build with new qemu kernels?
# Exclude it as temporary workaround.

IMAGE_INSTALL_remove_qemuall = "ccsp-hotspot-kmod"

# ----------------------------------------------------------------------------
