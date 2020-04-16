
# Prevent syslogd from being run automatically.

INITSCRIPT_PACKAGES_remove = "${PN}-syslog"

SYSTEMD_SERVICE_${PN}-syslog_remove = "busybox-syslog.service"

# If busybox-syslog.service is removed from SYSTEMD_SERVICE (as above)
# then it won't get packaged. Add an explicit rule to avoid QA warnings.

FILES_${PN}-syslog += "${systemd_unitdir}/system/busybox-syslog.service ${systemd_unitdir}/system/busybox-klogd.service"
