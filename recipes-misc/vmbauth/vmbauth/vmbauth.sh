#!/bin/sh

get_next_mac()
{
  local iface=$1
  IFS=: read -r -a mac < /sys/class/net/$iface/address
  for i in $(seq 5 -1 0); do
    mac[i]=$(printf '%02x\n' $(((0x${mac[i]} + 1) & 0xff)));
    [ "${mac[i]}" != "00" ] && break;
  done
  echo ${mac[0]}:${mac[1]}:${mac[2]}:${mac[3]}:${mac[4]}:${mac[5]}
}

delete_vmb_tunnel()
{
  set -x
  ip link del dev vmb0
  echo delif vmb0 wan > /proc/driver/flowmgr/cmd
  set +x
}

vmbauth_stop()
{
  start-stop-daemon -K -n vmbauth
  # sysevent set vmb-mode stop
  vmb-mode.sh stop > /tmp/vmb-radius-client/vmb-mode.log 2>&1
  delete_vmb_tunnel >> /tmp/vmb-radius-client/vmb-mode.log 2>&1
}

setup_vmb_tunnel()
{
  set -x

  local localip=$(ip -4 a l dev erouter0 | awk '$1=="inet" {print $2}' | awk -F / '{print $1}')
  local remoteip=$(syscfg get tunneled_static_ip_authserver)

  [ -n "$localip" ] || { echo "failed to get local ip"; return 1; }
  [ -n "$remoteip" ] || { echo "failed to get remote ip"; return 1; }
  [ -d /sys/class/net/erouter0 ] || { echo "erouter is not up"; return 1; }
  ip link add vmb0 address $(get_next_mac erouter0) txqueuelen 1000 mtu 1462 type gretap remote $remoteip local $localip dev erouter0 nopmtudisc
  echo 1 > /proc/sys/net/ipv6/conf/vmb0/disable_ipv6
  ip link set vmb0 up
  echo addif vmb0 wan > /proc/driver/flowmgr/cmd

  set +x
}

vmbauth_start()
{

  date > /tmp/vmb-radius-client/vmbauth.log
  setup_vmb_tunnel > /tmp/vmb-radius-client/vmb-mode.log 2>&1
  if [ -e /tmp/vmb-radius-client/vmbauth.skip.log ]; then
    # we have a hardcoded response file. Skipping the RADIUS authentication phase
    cp -a /tmp/vmb-radius-client/vmbauth.skip.log /tmp/vmb-radius-client/vmbauth.log
    vmb-mode.sh restart >> /tmp/vmb-radius-client/vmb-mode.log 2>&1
  else
    start-stop-daemon -S -b -n vmbauth vmbauth -- /tmp/vmb-radius-client/radiusclient.conf /tmp/vmb-radius-client/vmbauth.log erouter0 vmb0
  fi
}

vmbauth()
{
  vmbauth_stop
  if [ "$(syscfg get tunneled_static_ip_enable)" = "1" ]; then
    vmbauth_start
  fi
}

(flock 200; (exec 200>&-; vmbauth $*;) ) 200> /tmp/vmb-radius-client/vmbauth.lock &
