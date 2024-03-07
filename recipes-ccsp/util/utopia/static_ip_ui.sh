#/bin/sh

echo "static ui script"

if [ -d "/sys/devices/virtual/net/ovs-system" ]
then
	ovs_enable="true"
else
	ovs_enable="false"
fi

cmdiag_if=$(syscfg get cmdiag_ifname)
bridge_name=$(syscfg get lan_ifname)

if [ "$(syscfg get staticipadminstatus)" = "3" ] || { [ "$(syscfg get tunneled_static_ip_enable)" = "1" ] && [ "$(syscfg get brlan_static_ip_enable)" = "true" ]; }
then
	if [ "$ovs_enable" = "true" ]
	then
		bridgeCheck=$(ovs-vsctl show | grep -c "l${cmdiag_if}")
	else
		bridgeCheck=$(brctl show $bridge_name | grep -c "l${cmdiag_if}")
	fi

	if [ "$bridgeCheck" -ne 0 ]
	then
		echo "Multistatic UI is already enabled"
		exit 0
	fi

	ip link add ${cmdiag_if} type veth peer name l${cmdiag_if}

	cmdiag_mac=$(cat /sys/class/net/${cmdiag_if}/address)

	ifconfig ${cmdiag_if} hw ether ${cmdiag_mac}
	ifconfig l${cmdiag_if} promisc up
	ifconfig ${cmdiag_if} 192.168.0.1 netmask 255.255.255.0 up

	if [ "$ovs_enable" = "true" ]
	then
		cmd="ovs-vsctl add-port ${bridge_name} l${cmdiag_if}"
	else
		cmd="brctl addif ${bridge_name} l${cmdiag_if}"
	fi

	timeout=60

	while ! eval "$cmd"
	do
		timeout=$((timeout-1))
		if [ $timeout -eq 0 ]
		then
			break
		fi
		echo "Waiting for interface to be ready..."
		sleep 1
	done

	#---------------------------------------------------------------------------------------------
	# Redirect traffic destined to lan0 IP to lan0 MAC address from brlan0
	#---------------------------------------------------------------------------------------------
	if [ "$ovs_enable" = "true" ]
	then
		ovs-ofctl add-flow ${bridge_name} "priority=100,ip,nw_dst=192.168.0.1,actions=set_field:${cmdiag_mac}->eth_dst,output:l${cmdiag_if}"
	else
		ebtables -t nat -N STATICIP_REDIRECT
		ebtables -t nat -F STATICIP_REDIRECT 2> /dev/null
		ebtables -t nat -I PREROUTING -j STATICIP_REDIRECT

		ebtables -t nat -A STATICIP_REDIRECT --logical-in ${bridge_name} -p ipv4 --ip-dst 192.168.0.1 -j dnat --to-destination ${cmdiag_mac}
	fi

elif [ -f /sys/class/net/${cmdiag_if}/address ]
then
	ifconfig ${cmdiag_if} down
	ifconfig l${cmdiag_if} down
	ip link del ${cmdiag_if}

	if [ "$ovs_enable" = "true" ]
	then
		ovs-vsctl del-port ${bridge_name} l${cmdiag_if}
		ovs-ofctl del-flows ${bridge_name} "ip,nw_dst=192.168.0.1"
	else
		ebtables -t nat -D PREROUTING -j STATICIP_REDIRECT
		ebtables -t nat -X STATICIP_REDIRECT
	fi
fi
