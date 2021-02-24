SUMMARY = "Installable toolchain for SK"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit populate_sdk

TOOLCHAIN_TARGET_TASK_append = " packagegroup-sk-build-deps"
