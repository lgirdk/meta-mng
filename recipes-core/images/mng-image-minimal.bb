SUMMARY = "MNG Minimal Image"

IMAGE_FEATURES = "read-only-rootfs"
IMAGE_FEATURES += "debug-tweaks"

# Bash is required in RDKB based images. Using it as the shell in minimal
# images too means shell support in busybox can be completely disabled.

IMAGE_INSTALL = " \
    bash \
    packagegroup-core-boot \
"

IMAGE_INSTALL_append_qemuall = " strace"

BAD_RECOMMENDATIONS += "ca-certificates"

BAD_RECOMMENDATIONS += "openssl-conf"

SPLASH = ""

inherit image

# ----------------------------------------------------------------------------

PROJECT_BRANCH ?= "ofw-2206.7"

python create_version_file() {
    version_file = os.path.join(d.getVar("IMAGE_ROOTFS", True), 'version.txt')
    image_name = d.getVar("IMAGE_NAME", True)
    branch = d.getVar("PROJECT_BRANCH", True)
    release_version = d.getVar("RELEASE_VERSION", True) or '0.0.0.0'
    release_spin = d.getVar("RELEASE_SPIN", True) or '0'
    stamp = d.getVar("DATETIME", True)
    t = time.strptime(stamp, '%Y%m%d%H%M%S')
    build_time = time.strftime('"%Y-%m-%d %H:%M:%S"', t)
    with open(version_file, 'w') as fw:
        fw.write('imagename="{0}"\n'.format(image_name))
        fw.write('BRANCH="{0}"\n'.format(branch))
        fw.write('VERSION="{0}"\n'.format(release_version))
        fw.write('SPIN="{0}"\n'.format(release_spin))
        fw.write('BUILD_TIME={0}\n'.format(build_time))
}

create_version_file[vardepsexclude] += "DATETIME"

python version_hook(){
    bb.build.exec_func('create_version_file', d)
}

ROOTFS_POSTPROCESS_COMMAND += "version_hook; "

# ----------------------------------------------------------------------------

remove_unused_runlevels() {

	# Busybox init doesn't have any support for runlevels; only /etc/rcS
	# and /etc/rc5 (startup) and /etc/rc6 (shutdown) scripts are needed.

	rm -rf ${IMAGE_ROOTFS}/etc/rc0.d \
	       ${IMAGE_ROOTFS}/etc/rc1.d \
	       ${IMAGE_ROOTFS}/etc/rc2.d \
	       ${IMAGE_ROOTFS}/etc/rc3.d \
	       ${IMAGE_ROOTFS}/etc/rc4.d
}

ROOTFS_POSTPROCESS_COMMAND += "${@bb.utils.contains('VIRTUAL-RUNTIME_init_manager','busybox','remove_unused_runlevels;','',d)}"

# ----------------------------------------------------------------------------
