
# Drop elf support (avoids dependency on elfutils / libelf)

PACKAGECONFIG_remove_class-target = "elf"

# Drop tipc support (avoids dependency on libmnl)

PACKAGECONFIG_remove_class-target = "tipc"
