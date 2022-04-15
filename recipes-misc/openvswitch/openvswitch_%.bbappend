
# Undo meta-virtualization/recipes-networking/openvswitch/openvswitch.inc
# OFW uses busybox versions of sed awk and grep, so avoid pulling in the
# non-busybox versions.

RDEPENDS_${PN}_remove = "sed gawk grep"
