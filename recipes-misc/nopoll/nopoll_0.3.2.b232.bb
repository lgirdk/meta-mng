##########################################################################
# If not stated otherwise in this file or this component's Licenses.txt
# file the following copyright and licenses apply:
#
# Copyright 2016 RDK Management
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
##########################################################################

DESCRIPTION = "Recipe to build nopoll"
HOMEPAGE = "http://www.aspl.es/nopoll/"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f0504124678c1b3158146e0630229298 \
                   "
DEPENDS = "openssl"
LDFLAGS = "-pthread"
SRCREV = "6c2a10d9ad33c9229291a976db721950a4825af7"
SRC_URI = "git://github.com/Comcast/nopoll.git;branch=nopoll_yocto"

SRC_URI[md5sum] = "0f1ee40491e69d09b354771c6d44bb34"
SRC_URI[sha256sum] = "a1a25dcfe8406fcd355568ab0331f24eeec011a7b272df7b34a16db4a283b834"

SRC_URI_append_morty = " file://libnopoll.pc"

inherit autotools pkgconfig

S = "${WORKDIR}/git"

do_install_append_morty() {
        install -Dm644 ${S}/../libnopoll.pc ${D}${libdir}/pkgconfig/libnopoll.pc
}

FILES_${PN} = "${bindir} ${libdir}/lib*${SOLIBS}"

