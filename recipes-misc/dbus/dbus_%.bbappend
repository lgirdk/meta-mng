
# Force the systemd distro feature to be ignored to prevent systemd service
# files from being enabled. For RDKB, D-Bus will be started from RDKB init
# scripts (e.g. utopia_init.sh).
DISTRO_FEATURES_remove = "systemd"

# First introduced in D-Bus 1.9.14, the --enable-user-session option can be
# enabled by OS integrators intending to use systemd to provide a session bus
# per user. Note that all the configure option actually does is install some
# systemd service files to run the D-Bus daemon with the user-session config
# files. It's not required for RDKB (and the systemd service files it installs
# are not useful without systemd anyway).
PACKAGECONFIG_remove_class-target = "user-session"

# The RDKB usage of D-Bus causes failures in D-Bus's internal sanity checks.
# As a temporary workaround, disable them.
EXTRA_OECONF += "--disable-checks"

do_install_append_class-target () {

	# If /usr/share/dbus-1/system.conf exists (D-Bus 1.9.18 and later?)
	# then /etc/dbus-1/system.conf is an empty placeholder.
	# See: https://github.com/freedesktop/dbus/blob/master/NEWS
	# Process both to be compatible with all versions of D-Bus.

	for d in ${sysconfdir} ${datadir}
	do
		# ------------------------------------------------------------
		# system.conf  : config for the systemwide message bus
		# session.conf : config for the per-user-login-session message bus (not used for RDKB)
		# ------------------------------------------------------------

		# Blow some holes in the default systemwide message bus security policy...

		sed -e 's|<deny own="\*"/>|<allow own="*"/>|' \
		    -e 's|<deny send_type="method_call"/>|<allow send_type="method_call"/>|' \
		    -i ${D}$d/dbus-1/system.conf

		# Remove the per-user-login-session config files

		if ! ${@bb.utils.contains('PACKAGECONFIG','user-session','true','false',d)}
		then
			rm -f ${D}$d/dbus-1/session.conf
			rm -rf ${D}$d/dbus-1/session.d
		fi
	done

	# Remove DTD files (not required in the target rootfs)

	rm -rf ${D}${datadir}/xml
}

# ----------------------------------------------------------------------------
