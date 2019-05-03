
# Use the LSB_DISTRO_ADJUST hook to make Linux Mint appear to be Ubuntu (so
# that sstate cache from an Ubuntu system can be reused by a Linux Mint system
# directly, without needing to e.g. symlink subdirectories under sstate-cache).
#
# Note that the LinuxMint distro ID can be either capitalised or lower case,
# depending on the exact version of LinuxMint and whether the ID is fetched
# from /etc/os-release or from "lsb_release -ir". Try to handle all cases.

def lsb_distro_adjust(distro_id, ver):

    if (distro_id.lower() == 'linuxmint'):
        if ver in [ '17', '17.1', '17.2', '17.3' ]:
            distro_id = 'Ubuntu'
            ver = '14.04'
        if ver in [ '18', '18.1', '18.2', '18.3' ]:
            distro_id = 'Ubuntu'
            ver = '16.04'
        if ver in [ '19', '19.1', '19.2', '19.3' ]:
            distro_id = 'Ubuntu'
            ver = '18.04'
        if ver in [ '20', '20.1', '20.2', '20.3' ]:
            distro_id = 'Ubuntu'
            ver = '20.04'

    return distro_id, ver

LSB_DISTRO_ADJUST = "lsb_distro_adjust"
