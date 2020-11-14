
do_custom_users() {

	# --------------------------------------------------------------------

	mv ${S}/passwd.master ${S}/passwd.master.orig
	mv ${S}/group.master ${S}/group.master.orig

	# --------------------------------------------------------------------

	# Over-write the original files. The original passwd file contain
	# various system users etc with their login shell set to /bin/sh (which
	# we don't really want). Reduce to the bare minimum. Note that recipes
	# can also add users, so this isn't the final list.

	# Second field empty : Empty password (ie login doesn't require a password)
	# Second field = '*' : No password (ie not possible to login with a password)
	# Second field = 'x' : Password stored elsewhere (e.g. in /etc/shadow)

	echo 'root::0:0:root:/root:/bin/sh' > ${S}/passwd.master
	echo 'nobody:*:65534:65534::/nonexistent:/bin/false' >> ${S}/passwd.master

	# --------------------------------------------------------------------

	# Keep the "mail" group as it's required by fixup_perms() in
	# openembedded-core/meta/classes/package.bbclass (which sets file
	# permissions based on openembedded-core/meta/files/fs-perms.txt,
	# and fails if the "mail" group doesn't exist). Note that recipes
	# can also create groups, so this isn't the final list.

	echo 'root:*:0:' > ${S}/group.master
	echo 'mail:*:8:' >> ${S}/group.master
	echo 'nogroup:*:65534:' >> ${S}/group.master

	# --------------------------------------------------------------------
}

# Note: Source file modifications should be made as part of do_patch (rather
# than as part of do_configure etc) to avoid potential problems with sstate
# cache.

do_patch_append() {
    # This is python, not shell, so indent with 4 spaces
    bb.build.exec_func('do_custom_users', d)
}
