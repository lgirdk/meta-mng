
require recipes-ccsp/ccsp/ccsp_common_internal.inc

DEPENDS += "ccsp-common-library"
CFLAGS += "-I${STAGING_INCDIR}/ccsp"
