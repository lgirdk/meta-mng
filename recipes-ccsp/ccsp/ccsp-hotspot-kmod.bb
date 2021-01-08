SUMMARY = "CCSP Hotspot Kernel Module"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=90a09ab320e2368b0ee7213fd5be2d5c"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/mtu-modifier${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit module

# Fixme: merged from meta-cmf-broadband .bbappend. Is it still required ?

EXTRA_OEMAKE += " \
    SYSROOT=${STAGING_KERNEL_DIR} \
    KERNEL_BUILD=${STAGING_KERNEL_DIR} \
"

PACKAGES += "kernel-module-${PN}"
