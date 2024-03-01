require busybox.inc

SRC_URI = "https://busybox.net/downloads/busybox-${PV}.tar.bz2;name=tarball \
           file://busybox-udhcpc-no_deconfig.patch \
           file://busybox-cron \
           file://busybox-httpd \
           file://busybox-udhcpd \
           file://default.script \
           file://simple.script \
           file://hwclock.sh \
           file://syslog \
           file://syslog-startup.conf \
           file://syslog.conf \
           file://busybox-syslog.default \
           file://mdev \
           file://mdev.conf \
           file://mdev-mount.sh \
           file://defconfig \
           file://busybox-syslog.service.in \
           file://busybox-klogd.service.in \
           file://fail_on_no_media.patch \
           file://run-ptest \
           file://inetd.conf \
           file://inetd \
           file://recognize_connmand.patch \
           file://busybox-cross-menuconfig.patch \
           file://0001-Use-CC-when-linking-instead-of-LD-and-use-CFLAGS-and.patch \
           ${@["", "file://init.cfg"][(d.getVar('VIRTUAL-RUNTIME_init_manager', True) == 'busybox')]} \
           ${@["", "file://mdev.cfg"][(d.getVar('VIRTUAL-RUNTIME_dev_manager', True) == 'busybox-mdev')]} \
           file://syslog.cfg \
           file://rcS \
           file://rcK \
           file://watchdog \
           file://watchdog.service.in \
           file://makefile-libbb-race.patch \
           file://0001-testsuite-check-uudecode-before-using-it.patch \
           file://0001-testsuite-use-www.example.org-for-wget-test-cases.patch \
           file://0001-du-l-works-fix-to-use-145-instead-of-144.patch \
           file://0001-sysctl-ignore-EIO-of-stable_secret-below-proc-sys-ne.patch \
           file://0001-ash-fix-unset_var-pattern-repl.patch \
           file://0001-decompress_gunzip-Fix-DoS-if-gzip-is-corrupt.patch \
           file://0001-hwclock-make-glibc-2.31-compatible.patch \
           file://0001-awk-fix-dodgy-multi-char-separators-splitting-logic.patch \
           file://0002-awk-FS-regex-matches-only-non-empty-separators-gawk-.patch \
           file://CVE-2021-42374.patch \
           file://CVE-2021-42376.patch \
           file://0001-libbb-sockaddr2str-ensure-only-printable-characters-.patch \
           file://0002-nslookup-sanitize-all-printed-strings-with-printable.patch \
           file://CVE-2022-48174.patch \
           file://0001-traceroute-round-up-too-small-packet-sizes-non-stand.patch \
           file://0001-accept-truncated-ping-responses.patch \
           file://0001-udhcp-add-parameters-4-7-54-100-122-to-DHCP_REQUEST-.patch \
           file://0001-Fix-traceroute-error-invalid-Argument-when-port-numb.patch \
           file://ping_stuck.patch \
"
SRC_URI_append_libc-musl = " file://musl.cfg "

SRC_URI[tarball.md5sum] = "6273c550ab6a32e8ff545e00e831efc5"
SRC_URI[tarball.sha256sum] = "9d57c4bd33974140fd4111260468af22856f12f5b5ef7c70c8d9b75c712a0dee"
