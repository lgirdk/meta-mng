
require mng-image-minimal.bb

SUMMARY = "MNG Base Image"

IMAGE_INSTALL += " \
    bridge-utils \
    dibbler-server \
    dnsmasq \
    e2fsprogs-e2fsck \
    ebtables \
    ethtool \
    iproute2 \
    iptables \
    kernel-modules \
    lighttpd \
"

# Debug tools etc. Include tcpdump in non-systemd builds only (where image size
# is less of a concern).

IMAGE_INSTALL += " \
    dropbear \
    lrzsz \
    procps \
    tinymembench \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '', 'strace', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '', 'tcpdump', d)} \
"

# IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd-analyze', '', d)}"
# IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd-bootchart', '', d)}"

# ----------------------------------------------------------------------------
