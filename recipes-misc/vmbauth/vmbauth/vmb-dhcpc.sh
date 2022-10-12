#!/bin/sh

if [ "$1" = "bound" -o "$1" = "renew" ]; then
  IFS=. read -r i1 i2 i3 i4 <<< "$ip"
  IFS=. read -r m1 m2 m3 m4 <<< "$subnet"
  IFS=/ read -r curip curmask unused <<< "$(ip -4 a l dev vmb0 noprefixroute | awk '$1=="inet"{ print $2 }')"
  IFS=' ' read -r staticip staticipbits unused <<< "$(awk -F '[=/ ]' '$1=="Framed-Route" { print $2, $3 }' /tmp/vmb-radius-client/vmbauth.log)"

  if [ -z "$staticip" -o -z "$staticipbits" ]; then
    exit 0
  fi
  if [ "$ip" == "$curip" -a "$mask" == "$curmask" -a "$router" == "$(sysevent get vmb_gw_ip 2> /dev/null)" ]; then
    exit 0
  fi

  network="$((i1 & m1)).$((i2 & m2)).$((i3 & m3)).$((i4 & m4))"
  ip addr flush dev vmb0
  echo 1 > /proc/sys/net/ipv4/conf/vmb0/arp_announce
  ip addr add $ip/$mask noprefixroute dev vmb0
  [ "$staticipbits" -eq 32 ] && ip addr add ${staticip}/32 dev vmb0
  ip route add $network/$mask dev vmb0 table vmb
  ip route add default via $router dev vmb0 table vmb
  ip rule list lookup vmb | awk '!/iif|oif/{ $1=""; print; }' | \
  while read -r rule; do
    ip rule del $rule
  done
  ip rule add to $router lookup vmb

  start-stop-daemon -K -n vmbping
  start-stop-daemon -S -b -n vmbping vmbping $router vmb0 10 3

  sysevent set vmb_gw_ip $router
  sysevent set firewall-restart
fi
