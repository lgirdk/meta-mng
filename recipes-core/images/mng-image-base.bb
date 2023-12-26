
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
"

# Debug tools etc.

IMAGE_INSTALL += " \
    dropbear \
    lrzsz \
    openssl-bin \
    procps \
"

# IMAGE_INSTALL += "tinymembench"

# IMAGE_INSTALL += "strace"
# IMAGE_INSTALL += "tcpdump"

# IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd-analyze', '', d)}"
# IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd-bootchart', '', d)}"

# ----------------------------------------------------------------------------
