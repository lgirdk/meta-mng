
# Dibbler is written in C++, therefore by default the dibbler packages all have
# a runtime dependency on libstdc++, which means an ~1MB library on the target.
#
# If only one of the dibbler packages is required and no other C++ based
# applications are going to be included in the image (both of which conditions
# are currently true for Puma6 ARM builds) then linking the dibbler apps
# statically with libstdc++ can save some space in the final rootfs image.

CPPFLAGS += "-ffunction-sections -fdata-sections"
LDFLAGS += "-Wl,--gc-sections -static-libstdc++"
