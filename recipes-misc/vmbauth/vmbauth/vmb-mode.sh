#!/bin/sh

set -x

instance=`dmcli eRT getvalues Device.IP.Interface.4.IPv4Address. | grep tunneled_static -B1 | head -n 1 | awk -F'.' '{print $6}'`

vmbmode_stop()
{
  ip rule flush table vmb
  start-stop-daemon -K -n udhcpc -p /tmp/vmb-radius-client/udhcpc.pid
  start-stop-daemon -K -n vmbping
  syscfg unset vmb_ipaddress
  syscfg unset vmb_subnetmask
  if [ -n "$instance" ]; then
    dmcli eRT setv Device.IP.Interface.4.IPv4Address.$instance.Enable bool false
  fi
}

cidr_to_netmask() {
  local value=$(( 0xffffffff ^ ((1 << (32 - $1)) - 1) ))
  echo "$(( (value >> 24) & 0xff )).$(( (value >> 16) & 0xff )).$(( (value >> 8) & 0xff )).$(( value & 0xff ))"
}

vmbmode_start()
{
  local staticip=$(awk -F = '$1=="Framed-Route" { print $2 }' /tmp/vmb-radius-client/vmbauth.log | awk '{print $1}' | awk -F / '{print $1}')
  local staticipbits=$(awk -F = '$1=="Framed-Route" { print $2 }' /tmp/vmb-radius-client/vmbauth.log | awk '{print $1}' | awk -F / '{print $2}')

  syscfg set vmb_ipaddress "$staticip"
  syscfg set vmb_subnetmask "$(cidr_to_netmask $staticipbits)"

  ip rule add oif vmb0 lookup vmb
  ip rule add iif brlan0 lookup vmb

  udhcpc -i vmb0 -s /etc/vmb-dhcpc.sh -p /tmp/vmb-radius-client/udhcpc.pid

  if [ "$staticipbits" -ne 32 ]; then
    if [ ! -n "$instance" ]; then
      instance=`dmcli eRT addtable Device.IP.Interface.4.IPv4Address. | grep "added" | awk -F'.' '{print $6}'`
    fi
    IFS=. read -r ip1 ip2 ip3 ip4 <<< "$staticip"
    dmcli eRT setv Device.IP.Interface.4.IPv4Address.$instance.Alias string tunneled_static
    dmcli eRT setv Device.IP.Interface.4.IPv4Address.$instance.IPAddress string ${ip1}.${ip2}.${ip3}.$((ip4+1))
    dmcli eRT setv Device.IP.Interface.4.IPv4Address.$instance.SubnetMask string $(cidr_to_netmask $staticipbits)
  fi
}

case "$1" in
  start)
    vmbmode_start
    ;;
  stop)
    vmbmode_stop
    ;;
  restart)
    vmbmode_stop
    vmbmode_start
    ;;
  *)
    echo "Usage: vmb-mode.sh [ start | stop | restart ]"
    ;;
esac
