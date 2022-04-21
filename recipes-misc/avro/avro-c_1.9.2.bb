SUMMARY = "Apache Avro data serialization system."
HOMEPAGE = "http://apr.apache.org/"
SECTION = "libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6d502b41f76179fc84e536236f359cae"

DEPENDS = "jansson zlib xz"

SRC_URI = "https://archive.apache.org/dist/avro/avro-${PV}/c/avro-c-${PV}.tar.gz \
           file://0001-cmake-Use-GNUInstallDirs-instead-of-hard-coded-paths.patch \
"

SRC_URI[md5sum] = "f8cba983e36b0494608e4017a1da5311"
SRC_URI[sha256sum] = "08697f7dc9ff52829ff90368628a80f6fd5c118004ced931211c26001e080cd2"

inherit cmake lib_package
