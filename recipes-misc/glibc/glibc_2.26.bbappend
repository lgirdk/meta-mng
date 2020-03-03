
PROVIDES += "libucrpc"

do_install_append() {

	# Create a dummy libucrpc.so so that attempts to link with -lucrpc succeed.

	ln -sf libc.so ${D}${libdir}/libucrpc.so
}
