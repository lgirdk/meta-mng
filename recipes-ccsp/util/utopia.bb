SUMMARY = "CCSP Utopia"
LICENSE = "Apache-2.0 & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=baa21dec03307f641a150889224a157f"

require recipes-ccsp/ccsp/ccsp_common.inc

DEPENDS += "hal-ethsw hal-platform telemetry cjson nanomsg libevent libnetfilter-queue libsyswrapper libupnp"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES','core-net-lib','core-net-lib','',d)}"

# Utopia needs an RPC library in order to support IPC between the ARM and Atom
# CPUs in an Intel Puma 6 or Puma 7 (ie it's not thought to be required for
# other targets, even though the build dependency may still be there). The RPC
# library used by utopia should match the RPC library used by the low level
# components from the Intel SDK - ie the legacy RPC library included in uClibc
# and glibc.(or libucrpc, the standalone version), but NOT the newer libtircp.
# Linking with libtircp might fix the build dependency but it's not going to
# work well at run time...

DEPENDS_append = " libucrpc"
LDFLAGS_append = " -lucrpc"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${LGI_RDKB_GIT}/${BPN}${LGI_RDKB_GIT_SUFFIX};protocol=${LGI_RDKB_GIT_PROTOCOL}${LGI_RDKB_GIT_EXTRAOPT}"

SRC_URI += "file://udhcpc.script"
SRC_URI += "file://udhcpc.vendor_specific"
SRC_URI += "file://dhcpswitch.sh"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig useradd update-alternatives

LEGACY_PLATFORM ?= "0"

EXTRA_OECONF += "${CCSP_CONFIG_PLATFORM}"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'multilan', 'MULTILAN_FEATURE=yes', '', d)}"
EXTRA_OECONF += "--enable-core_net_lib_feature_support=${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', 'yes', 'no', d)}"
EXTRA_OECONF += "--enable-ddns_binary_client_support"

CFLAGS += " \
    -DCONFIG_BUILD_TRIGGER \
    -DXT_TIME_MODULE_SUPPORT \
    ${@bb.utils.contains('DISTRO_FEATURES','bci','-DCISCO_CONFIG_TRUE_STATIC_IP -DCISCO_CONFIG_DHCPV6_PREFIX_DELEGATION','',d)} \
"

do_install_append () {

    # ------------------------------------------------------------------------

    install -d ${D}${includedir}/syscfg
    install -d ${D}${includedir}/sysevent
    install -d ${D}${includedir}/ulog
    install -d ${D}${includedir}/utapi
    install -d ${D}${includedir}/utctx

    install -m 644 ${S}/source/syscfg/lib/syscfg.h ${D}${includedir}/syscfg/
    install -m 644 ${S}/source/sysevent/lib/sysevent.h ${D}${includedir}/sysevent/
    install -m 644 ${S}/source/sysevent/lib/libsysevent_internal.h ${D}${includedir}/sysevent/
    install -m 644 ${S}/source/ulog/ulog.h ${D}${includedir}/ulog/
    install -m 644 ${S}/source/utapi/lib/*.h ${D}${includedir}/utapi/
    install -m 644 ${S}/source/utctx/lib/utctx.h ${D}${includedir}/utctx/
    install -m 644 ${S}/source/utctx/lib/utctx_api.h ${D}${includedir}/utctx/
    install -m 644 ${S}/source/utctx/lib/utctx_rwlock.h ${D}${includedir}/utctx/

    # The autoconf.h inside the utopia source tree is private to utopia and
    # shouldn't be installed, but some public headers expect to include it so
    # create an empty version (fixme: needs review).

    touch ${D}${includedir}/utctx/autoconf.h

    # ------------------------------------------------------------------------

    install -d ${D}${includedir}/ccsp
    install -m 644 ${S}/source/util/print_uptime/print_uptime.h ${D}${includedir}/ccsp/

    # ------------------------------------------------------------------------

    install -d ${D}${sysconfdir}/utopia
    install -m 644 ${WORKDIR}/udhcpc.vendor_specific ${D}${sysconfdir}/
    install -m 755 ${WORKDIR}/udhcpc.script ${D}${sysconfdir}/
    install -m 755 ${WORKDIR}/dhcpswitch.sh ${D}${sysconfdir}/

    install -d ${D}${sysconfdir}/cron/cron.everyminute
    install -d ${D}${sysconfdir}/cron/cron.every5minute
    install -d ${D}${sysconfdir}/cron/cron.every10minute
    install -d ${D}${sysconfdir}/cron/cron.hourly
    install -d ${D}${sysconfdir}/cron/cron.daily
    install -d ${D}${sysconfdir}/cron/cron.weekly
    install -d ${D}${sysconfdir}/cron/cron.monthly

    install -m 644 ${S}/source/scripts/init/service.d/cron.allow ${D}${sysconfdir}/cron/
    install -m 755 ${S}/source/scripts/init/service.d/logrotate.sh ${D}${sysconfdir}/cron/cron.every5minute/
    install -m 755 ${S}/source/scripts/init/service.d/pmon_every5minute.sh ${D}${sysconfdir}/cron/cron.every5minute/
    install -m 755 ${S}/source/scripts/init/service.d/log_every10minute.sh ${D}${sysconfdir}/cron/cron.every10minute/
    install -m 755 ${S}/source/scripts/init/service.d/log_hourly.sh ${D}${sysconfdir}/cron/cron.hourly/
    install -m 755 ${S}/source/scripts/init/service.d/ntp_hourly.sh ${D}${sysconfdir}/cron/cron.hourly/
    install -m 755 ${S}/source/scripts/init/service.d/ddns_daily.sh ${D}${sysconfdir}/cron/cron.daily/
    install -m 755 ${S}/source/scripts/init/service.d/remove_max_cpu_usage_file.sh ${D}${sysconfdir}/cron/cron.daily/

    # Create alias symlinks for syscfg multicall binary

    for app in create destroy ; do
        ln -sf ${bindir}/syscfg ${D}${bindir}/syscfg_$app
    done

    # Define the RDKB specific local routing tables (used by iproute2)

    install -d ${D}${sysconfdir}/iproute2/rt_tables.d
    { echo "3 erouter" ; echo "4 all_lans" ; echo "6 moca" ; echo "7 brmode" ; } > ${D}${sysconfdir}/iproute2/rt_tables.d/rdkb.conf

    # Create mount point for tmpfs which will store RDK log files etc

    install -d ${D}/rdklogs

    install -m 644 ${S}/source/scripts/init/defaults/system_defaults_arm ${D}${sysconfdir}/utopia/system_defaults

    # Strip whitespace and comments etc from system_defaults

    sed 's/#.*//; s/[ \t]\+$//; /^$/d' -i ${D}${sysconfdir}/utopia/system_defaults

    # Sort all entries but keep Version as the first line since it's searched
    # for first, before a second pass to parse the rest of the entries.

    grep '^\$\$Version=' ${D}${sysconfdir}/utopia/system_defaults > ${D}${sysconfdir}/utopia/system_defaults_new
    grep -v '^\$\$Version=' ${D}${sysconfdir}/utopia/system_defaults | LC_ALL=C sort >> ${D}${sysconfdir}/utopia/system_defaults_new
    mv ${D}${sysconfdir}/utopia/system_defaults_new ${D}${sysconfdir}/utopia/system_defaults

    # Tweak defaults for backwards compatibility on legacy platforms

    if [ "${LEGACY_PLATFORM}" = "1" ]; then
        sed 's/^\$\$arLanAllowedSubnet_1::SubnetMask=.*/$$arLanAllowedSubnet_1::SubnetMask=255.255.0.0/' -i ${D}${sysconfdir}/utopia/system_defaults
    fi

    # ------------------------------------------------------------------------

    install -m 644 ${S}/source/scripts/init/syslog_conf/syslog.conf_default ${D}${sysconfdir}/syslog.conf.${BPN}

    # ------------------------------------------------------------------------

    install -d ${D}${sysconfdir}/utopia/service.d
    install -d ${D}${sysconfdir}/utopia/service.d/service_bridge
    install -d ${D}${sysconfdir}/utopia/service.d/service_cloudui
    install -d ${D}${sysconfdir}/utopia/service.d/service_ddns
    install -d ${D}${sysconfdir}/utopia/service.d/service_dhcp_server
    install -d ${D}${sysconfdir}/utopia/service.d/service_lan
    install -d ${D}${sysconfdir}/utopia/service.d/service_multinet
    install -d ${D}${sysconfdir}/utopia/service.d/service_syslog
    install -d ${D}${sysconfdir}/utopia/service.d/service_wan

    install -m 755 ${S}/source/scripts/init/system/utopia_init.sh                               ${D}${sysconfdir}/utopia/

    install -m 755 ${S}/source/scripts/init/service.d/*.sh                                      ${D}${sysconfdir}/utopia/service.d/
    install -m 644 ${S}/source/scripts/init/service.d/event_flags                               ${D}${sysconfdir}/utopia/service.d/
    install -m 755 ${S}/source/scripts/init/service.d/service_firewall/firewall_log_handle.sh   ${D}${sysconfdir}/utopia/service.d/
    install -m 755 ${S}/source/scripts/init/service.d/service_bridge/*.sh                       ${D}${sysconfdir}/utopia/service.d/service_bridge/
    install -m 755 ${S}/source/scripts/init/service.d/service_cloudui/*.sh                      ${D}${sysconfdir}/utopia/service.d/service_cloudui/
    install -m 755 ${S}/source/scripts/init/service.d/service_ddns/*.sh                         ${D}${sysconfdir}/utopia/service.d/service_ddns/
    install -m 755 ${S}/source/scripts/init/service.d/service_dhcp_server/*                     ${D}${sysconfdir}/utopia/service.d/service_dhcp_server/
    install -m 755 ${S}/source/scripts/init/service.d/service_lan/*.sh                          ${D}${sysconfdir}/utopia/service.d/service_lan/
    install -m 755 ${S}/source/scripts/init/service.d/service_multinet/*.sh                     ${D}${sysconfdir}/utopia/service.d/service_multinet/
    install -m 755 ${S}/source/scripts/init/service.d/service_syslog/*.sh                       ${D}${sysconfdir}/utopia/service.d/service_syslog/
    install -m 755 ${S}/source/scripts/init/service.d/service_wan/*.sh                          ${D}${sysconfdir}/utopia/service.d/service_wan/

    # nat_passthrough.sh gets installed into /etc/utopia/service.d by the *.sh
    # wildcard install above, but it should be in /etc/utopia

    mv ${D}${sysconfdir}/utopia/service.d/nat_passthrough.sh                                    ${D}${sysconfdir}/utopia/

    # waninfo.sh gets installed into /etc/utopia/service.d by the *.sh
    # wildcard install above, but it should be in /etc

    mv ${D}${sysconfdir}/utopia/service.d/waninfo.sh                                            ${D}${sysconfdir}/

    # Over-write the default scripts installed above with the _arm versions.
    # Note that _arm versions are not necessarily ARM CPU specific, but they
    # are generally the most well maintained and up to date.

    mv -f ${D}${sysconfdir}/utopia/service.d/service_cosa_arm.sh                                ${D}${sysconfdir}/utopia/service.d/service_cosa.sh
    mv -f ${D}${sysconfdir}/utopia/service.d/service_dhcpv6_client_arm.sh                       ${D}${sysconfdir}/utopia/service.d/service_dhcpv6_client.sh

    # Over-write the default handle_gre.sh with the handle_gre_lg.sh version

    mv -f ${D}${sysconfdir}/utopia/service.d/service_multinet/handle_gre_lg.sh                  ${D}${sysconfdir}/utopia/service.d/service_multinet/handle_gre.sh

    # Remove service_gre.sh ( Intel specific - see INTEL_GRE_HOTSPOT )

    rm ${D}${sysconfdir}/utopia/service.d/service_multinet/service_gre.sh

    ln -sf /usr/bin/service_multinet_exec                                                       ${D}${sysconfdir}/utopia/service.d/service_multinet_exec

    install -d ${D}${sbindir}
    install -m 755 ${S}/source/scripts/init/syslog_conf/log_start.sh                            ${D}${sbindir}/
    install -m 755 ${S}/source/scripts/init/syslog_conf/log_handle.sh                           ${D}${sbindir}/
    install -m 755 ${S}/source/scripts/init/syslog_conf/syslog_conf_tool.sh                     ${D}${sbindir}/

    install -m 755 ${S}/source/scripts/init/system/need_wifi_default.sh                         ${D}${sysconfdir}/utopia/

    install -d ${D}${sysconfdir}/IGD
    install -m 644 ${S}/source/igd/src/inc/*.xml                                                ${D}${sysconfdir}/IGD/

    touch ${D}${sysconfdir}/dhcp_static_hosts

    # ------------------------------------------------------------------------

    # registration.d is the default directory for sysevent registration binaries. It's called once on bootup.
    # post.d binaries are called explicitly via e.g. "execute_dir /etc/utopia/post.d" or "execute_dir /etc/utopia/post.d restart"
    # Some binaries (e.g. 10_firewall) may be present in both.

    install -d ${D}${sysconfdir}/utopia/post.d
    install -d ${D}${sysconfdir}/utopia/registration.d

    ln -sf /usr/bin/02_bridge                   ${D}${sysconfdir}/utopia/registration.d/02_bridge
    ln -sf /usr/bin/02_forwarding               ${D}${sysconfdir}/utopia/registration.d/02_forwarding
    ln -sf /usr/bin/02_ipv4                     ${D}${sysconfdir}/utopia/registration.d/02_ipv4
    ln -sf /usr/bin/02_ipv6                     ${D}${sysconfdir}/utopia/registration.d/02_ipv6
    ln -sf /usr/bin/02_lanHandler               ${D}${sysconfdir}/utopia/registration.d/02_lanHandler
    ln -sf /usr/bin/02_multinet                 ${D}${sysconfdir}/utopia/registration.d/02_multinet
#   ln -sf /usr/bin/02_parodus                  ${D}${sysconfdir}/utopia/registration.d/02_parodus
    ln -sf /usr/bin/02_wan                      ${D}${sysconfdir}/utopia/registration.d/02_wan
#   ln -sf /usr/bin/09_xdns                     ${D}${sysconfdir}/utopia/registration.d/09_xdns
    ln -sf /usr/bin/10_firewall                 ${D}${sysconfdir}/utopia/registration.d/10_firewall
#   ln -sf /usr/bin/10_ntpd                     ${D}${sysconfdir}/utopia/registration.d/10_ntpd
    ln -sf /usr/bin/15_ccsphs                   ${D}${sysconfdir}/utopia/registration.d/15_ccsphs
#   ln -sf /usr/bin/15_ddnsclient               ${D}${sysconfdir}/utopia/registration.d/15_ddnsclient
    ln -sf /usr/bin/15_dhcp_server              ${D}${sysconfdir}/utopia/registration.d/15_dhcp_server
    ln -sf /usr/bin/15_dhcpv6_client            ${D}${sysconfdir}/utopia/registration.d/15_dhcpv6_client
#   ln -sf /usr/bin/15_dhcpv6_server            ${D}${sysconfdir}/utopia/registration.d/15_dhcpv6_server
    ln -sf /usr/bin/15_dynamic_dns              ${D}${sysconfdir}/utopia/registration.d/15_dynamic_dns
    ln -sf /usr/bin/15_hotspot                  ${D}${sysconfdir}/utopia/registration.d/15_hotspot
    ln -sf /usr/bin/15_misc                     ${D}${sysconfdir}/utopia/registration.d/15_misc
#   ln -sf /usr/bin/15_ssh_server               ${D}${sysconfdir}/utopia/registration.d/15_ssh_server
#   ln -sf /usr/bin/15_wecb                     ${D}${sysconfdir}/utopia/registration.d/15_wecb
    ln -sf /usr/bin/20_routing                  ${D}${sysconfdir}/utopia/registration.d/20_routing
    ln -sf /usr/bin/25_crond                    ${D}${sysconfdir}/utopia/registration.d/25_crond
#   ln -sf /usr/bin/26_potd                     ${D}${sysconfdir}/utopia/registration.d/26_potd
    ln -sf /usr/bin/33_cosa                     ${D}${sysconfdir}/utopia/registration.d/33_cosa

    ln -sf /usr/bin/10_firewall                 ${D}${sysconfdir}/utopia/post.d/10_firewall
    ln -sf /usr/bin/10_mcastproxy               ${D}${sysconfdir}/utopia/post.d/10_mcastproxy
    ln -sf /usr/bin/10_mldproxy                 ${D}${sysconfdir}/utopia/post.d/10_mldproxy
    ln -sf /usr/bin/15_igd                      ${D}${sysconfdir}/utopia/post.d/15_igd

    # ------------------------------------------------------------------------
}

ALTERNATIVE_PRIORITY = "190"
ALTERNATIVE_${PN} = "syslog-conf"
ALTERNATIVE_LINK_NAME[syslog-conf] = "${sysconfdir}/syslog.conf"
ALTERNATIVE_TARGET[syslog-conf] = "${sysconfdir}/syslog.conf.${BPN}"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --home ${localstatedir}/run/firewall -M --shell /bin/false --user-group firewall"

FILES_${PN} += "/rdklogs"

RDEPENDS_${PN} += "mcproxy"

# source/scripts/init/service.d/service_ntpd.sh contains calls to ntpq (which is not provided by busybox)
RDEPENDS_${PN} += "ntp ntpq"

RDEPENDS_${PN} += "customer-configs"

# source/firewall/nfq_handler.c contains calls to ipset
RDEPENDS_${PN} += "ipset"
