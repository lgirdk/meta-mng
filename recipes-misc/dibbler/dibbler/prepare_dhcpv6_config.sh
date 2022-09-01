#!/bin/sh

if [ -f /etc/device.properties ];then
     . /etc/device.properties
fi

VENDOR_SPEC_FILE="/etc/dibbler/udhcpc.vendor_specific"
OPTION_FILE="/tmp/vendor_spec.txt"
DHCP_CONFIG_FILE="/etc/dibbler/client.conf"
DHCP_CONFIG_FILE_RFS="/etc/dibbler/client.conf-basic"
DHCP_CONFIG_FILE_TMP="/tmp/dibbler/client.conf"
#interface=$ARM_INTERFACE

ethWanMode=`syscfg get eth_wan_enabled`
DSLite_Enabled=`syscfg get dslite_enable`
#interface is $ARM_INTERFACE which comes from device.properties
#interface=erouter0 is now specified in client.conf-basic
#if [ "$interface" ] && [ -f /etc/dibbler/client_back.conf ];then
#    sed -i "s/RDK-ESTB-IF/${interface}/g" /etc/dibbler/client_back.conf
#fi


if [ -f $OPTION_FILE ]; then
        rm -rf $OPTION_FILE
fi

updateOptInfo()
{
  opt_val=$1
  subopt_num=$2
  subopt_len=`echo ${#opt_val}`
  subopt_len_h=`printf "%04x\n" $subopt_len`;
  subopt_val_h=`echo -n $opt_val | hexdump -e '13/1 "%02x"'`
  echo -n $subopt_num$subopt_len_h$subopt_val_h >> $OPTION_FILE
  return
}

updateDUIDInfo()
{
  EnpNum=3561
  ProdClass=`dmcli eRT retv Device.DeviceInfo.ProductClass`
  MfrOUI=`dmcli eRT retv Device.DeviceInfo.ManufacturerOUI`
  SrNum=`dmcli eRT retv Device.DeviceInfo.X_LGI-COM_SerialNumber`
  Idntfr=`echo -n $MfrOUI-$ProdClass-$SrNum | hexdump -e '13/1 "%02x"'`
  echo "duid-type duid-en $EnpNum 0x$Idntfr"
}

if [ "$DSLite_Enabled" = "1" ];then
	echo  "        option aftr" >> $OPTION_FILE
fi

# Add Option: Vendor Class (16) with Enterprise ID: 4491 (0x118b), vendor-class-data: eRouter1.0 (length 0x000a + 0x65526f75746572312e30)
echo "        option 0016 hex 0x0000118b000a65526f75746572312e30" >> $OPTION_FILE

if [ "$EROUTER_DHCP_OPTION_EMTA_ENABLED" = "true" ] &&  [ "$ethWanMode" = "true" ];then 
	echo -n "        option 0017 hex 0x0000118b000100060027087A087B" >> $OPTION_FILE
else
	echo -n "        option 0017 hex 0x0000118b" >> $OPTION_FILE
fi

# Append option 17 suboption 1 : Option Request (1027)
echo -n "000100020403" >> $OPTION_FILE

# Append option 17 suboption 36 : Device Identifier (erouter0 mac address)
id_interface="erouter0"
id_mac=$(tr -d ':' < /sys/class/net/${id_interface}/address)
echo -n "00240006${id_mac}" >> $OPTION_FILE

    while read line
    do
        mode=`echo $line | cut -f1 -d" "`
        opt_num=`echo $line | cut -f2 -d" "`
        opt_val=`echo $line | cut -f3 -d" "`
        case "$opt_num" in
            "SUBOPTION2")
                subopt_num="0002"
                updateOptInfo $opt_val $subopt_num
                ;;
            "SUBOPTION3")
                subopt_num="0003"
                if [ "$EROUTER_DHCP_OPTION_EMTA_ENABLED" = "true" ]  ;then 
                        if [ "$mode" = "DOCSIS" ] && [ "$ethWanMode" = "true" ] ;then
                                continue;
                        fi

                        if [ "$mode" = "ETHWAN" ] && [ "$ethWanMode" = "false" ] ;then
                                continue;
                        fi
                elif [ "$mode" = "ETHWAN" ] ;then
                        continue;
                fi
                updateOptInfo $opt_val $subopt_num
                ;;
        esac;
    done < "$VENDOR_SPEC_FILE"

if [ "$EROUTER_DHCP_OPTION_EMTA_ENABLED" = "true" ] && [ "$ethWanMode" = "true" ];then 
    echo -n "0027000107" >> $OPTION_FILE
fi

log_level=`syscfg get dibbler_log_level`

if [ -z "$log_level" ] || [ $log_level -lt 1 ]; then
    log_level=1
elif [ $log_level -gt 8 ]; then
    log_level=4
fi

if [ -f "$DHCP_CONFIG_FILE_TMP" ]; then
    rm -rf $DHCP_CONFIG_FILE_TMP
fi

echo "log-level $log_level" > $DHCP_CONFIG_FILE_TMP

updateDUIDInfo >> $DHCP_CONFIG_FILE_TMP
sed '$d' $DHCP_CONFIG_FILE_RFS >> $DHCP_CONFIG_FILE_TMP
cat $OPTION_FILE >> $DHCP_CONFIG_FILE_TMP
echo >> $DHCP_CONFIG_FILE_TMP
echo "}" >> $DHCP_CONFIG_FILE_TMP

