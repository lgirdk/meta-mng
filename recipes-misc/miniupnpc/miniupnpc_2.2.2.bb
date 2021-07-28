SUMMARY = "miniUPnP Client"
HOMEPAGE = "http://miniupnp.free.fr/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=05ec39583d6dabb2ece9a780c7aa1f72"

SRC_URI = "http://miniupnp.tuxfamily.org/files/download.php?file=${BP}.tar.gz;downloadfilename=${BP}.tar.gz"

SRC_URI[md5sum] = "e13206be6bb3b8aa246d4dc07ab99511"
SRC_URI[sha256sum] = "888fb0976ba61518276fe1eda988589c700a3f2a69d71089260d75562afd3687"

inherit cmake

EXTRA_OECMAKE += "-DUPNPC_BUILD_STATIC=FALSE"
