SUMMARY = "Customer specific syscfg and PSM config files"
LICENSE = "CLOSED"

SRC_URI = " \
    file://lg_bbhm_cust_6.xml file://lg_syscfg_cust_6.db \
    file://lg_bbhm_cust_7.xml file://lg_syscfg_cust_7.db \
    file://lg_bbhm_cust_8.xml file://lg_syscfg_cust_8.db \
    file://lg_bbhm_cust_9.xml file://lg_syscfg_cust_9.db \
    file://lg_bbhm_cust_20.xml file://lg_syscfg_cust_20.db \
    file://lg_bbhm_cust_23.xml file://lg_syscfg_cust_23.db \
    file://lg_bbhm_cust_26.xml file://lg_syscfg_cust_26.db \
    file://lg_bbhm_cust_27.xml file://lg_syscfg_cust_27.db \
    file://lg_bbhm_cust_41.xml file://lg_syscfg_cust_41.db \
    file://lg_bbhm_cust_44.xml file://lg_syscfg_cust_44.db \
    file://lg_bbhm_cust_1001.xml file://lg_syscfg_cust_1001.db \
    file://lg_bbhm_cust_1002.xml file://lg_syscfg_cust_1002.db \
    file://lg_bbhm_cust_1003.xml file://lg_syscfg_cust_1003.db \
    file://lg_bbhm_cust_1004.xml file://lg_syscfg_cust_1004.db \
"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {

	install -d ${D}${sysconfdir}/utopia/defaults

	for file in `ls ${WORKDIR}/lg_bbhm_cust_*.xml ${WORKDIR}/lg_syscfg_cust_*.db`
	do
		install -m 644 $file ${D}${sysconfdir}/utopia/defaults/
	done
}

RPROVIDES_${PN} = "customer-configs"
