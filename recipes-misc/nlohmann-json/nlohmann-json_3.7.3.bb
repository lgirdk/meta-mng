SUMMARY = "JSON for Modern C++"
HOMEPAGE = "https://json.nlohmann.me/home/releases/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://json.hpp;endline=28;md5=a8b9e4ab9c6fe01ba7639330990ce709"

# The development repo is > 250MB, but the release is a single file.
# SRC_URI = "git://github.com/nlohmann/json.git;branch=develop"
# SRCREV = "e7b3b40b5a95bc74b9a7f662830a27c49ffc01b4"

SRC_URI = "https://github.com/nlohmann/json/releases/download/v3.7.3/json.hpp;subdir=${BP}"

SRC_URI[md5sum] = "39b754f6834e64406d7eae9dfb9e5d9e"
SRC_URI[sha256sum] = "3b5d2b8f8282b80557091514d8ab97e27f9574336c804ee666fda673a9b59926"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	install -d ${D}${includedir}/nlohmann
	install -m 644 ${S}/json.hpp ${D}${includedir}/nlohmann/
}

# nlohmann-json is a header only C++ library, so the default package will be empty.

ALLOW_EMPTY_${PN} = "1"
