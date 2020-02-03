
# ----------------------------------------------------------------------------

# use gold linker for ARM/Intel targets
DISTRO_FEATURES_append_arm = " ld-is-gold"
DISTRO_FEATURES_append_armeb = " ld-is-gold"
DISTRO_FEATURES_append_x86 = " ld-is-gold"

# ----------------------------------------------------------------------------
