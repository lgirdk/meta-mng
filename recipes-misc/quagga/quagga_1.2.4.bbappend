
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-fix-ngroups-value-passed-to-getgrouplist.patch"
SRC_URI += "file://0001-reduce-size-of-buffer-passed-to-getgrouplist.patch"
SRC_URI += "file://rtadv.patch"
SRC_URI += "file://adding-route-information-option-24.patch"
SRC_URI += "file://CPE-advertisement-of-RIP_routes-not-always-match-with-Update-Interval-time.patch"
