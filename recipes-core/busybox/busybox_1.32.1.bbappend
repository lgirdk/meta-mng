
# The OE 3.0 version of meta-clang applies this patch to all versions of
# busybox. It's not needed for busybox 1.32.x and above.

SRC_URI_remove = "file://0001-Turn-ptr_to_globals-and-bb_errno-to-be-non-const.patch"
