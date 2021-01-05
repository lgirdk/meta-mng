
# ----------------------------------------------------------------------------

RDK_RELEASE ?= "8888"

SRC_DIR = '${@os.path.normpath("${COREBASE}/../src")}'

SRC_URI_pn-breakpad-wrapper             = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-cm-agent                = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-common-library          = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-common-library-native   = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-cr                      = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-dmcli                   = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-epon-agent              = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-eth-agent               = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-gwprovapp               = "git://${SRC_DIR}/gwprovapp;protocol=file;usehead=1"
SRC_URI_pn-ccsp-gwprovapp-epon          = "git://${SRC_DIR}/gwprovapp-epon;protocol=file;usehead=1"
SRC_URI_pn-ccsp-gwprovapp-ethwan        = "git://${SRC_DIR}/gwprovapp-ethwan;protocol=file;usehead=1"
SRC_URI_pn-ccsp-home-security           = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-hotspot                 = "git://${SRC_DIR}/hotspot;protocol=file;usehead=1"
SRC_URI_pn-ccsp-hotspot-kmod            = "git://${SRC_DIR}/mtu-modifier;protocol=file;usehead=1"
SRC_URI_pn-ccsp-lm-lite                 = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-logagent                = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-misc                    = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-moca                    = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-mta-agent               = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-p-and-m                 = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-psm                     = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-snmp-pa                 = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-tr069-pa                = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-wifi-agent              = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-ccsp-xdns                    = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-dca                          = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-dcmjsonparser                = "git://${SRC_DIR}/dcm;protocol=file;usehead=1"
SRC_URI_pn-hal-cm-generic               = "git://${SRC_DIR}/hal;protocol=file;usehead=1"
SRC_URI_pn-hal-dhcpv4c-generic          = "git://${SRC_DIR}/hal;protocol=file;usehead=1"
SRC_URI_pn-hal-epon-generic             = "git://${SRC_DIR}/hal;protocol=file;usehead=1"
SRC_URI_pn-hal-ethsw-generic            = "git://${SRC_DIR}/hal;protocol=file;usehead=1"
SRC_URI_pn-hal-firewall-generic         = "git://${SRC_DIR}/hal;protocol=file;usehead=1"
SRC_URI_pn-hal-moca-generic             = "git://${SRC_DIR}/hal;protocol=file;usehead=1"
SRC_URI_pn-hal-mso-mgmt-generic         = "git://${SRC_DIR}/hal;protocol=file;usehead=1"
SRC_URI_pn-hal-mta-generic              = "git://${SRC_DIR}/hal;protocol=file;usehead=1"
SRC_URI_pn-hal-platform-generic         = "git://${SRC_DIR}/hal;protocol=file;usehead=1"
SRC_URI_pn-hal-vlan-generic             = "git://${SRC_DIR}/hal;protocol=file;usehead=1"
SRC_URI_pn-hal-wifi-generic             = "git://${SRC_DIR}/hal;protocol=file;usehead=1"
SRC_URI_pn-halinterface                 = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-libsyswrapper                = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-libunpriv                    = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-notify-comp                  = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-rbus                         = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-rbus-core                    = "git://${SRC_DIR}/rbuscore;protocol=file;usehead=1"
SRC_URI_pn-rdk-logger                   = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-rdklist                      = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-rtmessage                    = "git://${SRC_DIR}/rbuscore;protocol=file;usehead=1"
SRC_URI_pn-sysint-broadband             = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-syslog-helper                = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-telemetry                    = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-test-and-diagnostic          = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"
SRC_URI_pn-utopia                       = "git://${SRC_DIR}/${BPN};protocol=file;usehead=1"

# ----------------------------------------------------------------------------
#
# Even though the protocol is "file" (ie the source repo is already available
# in the local filesystem) the git fetcher will still create a clone of the
# source repo in the OE downloads directory. The repo name within the downloads
# directory will be based on the absolute path of the source repo, for example:
#
#   Original source repo       : /home/user/build-123/src/foo
#   Path of clone in downloads : ${DL_DIR}/git2/home.user.build-123.src.foo
#
# If the source repos are part of the directory structure created by
# "repo init ; repo sync" then their absolute path will change whenever
# "repo init ; repo sync" is run in a new directory. To avoid filling downloads
# with multiple clones of the local source repos, force the fetcher to create
# the clones under BASE_WORKDIR (ie under tmp) instead of the default downloads
# directory. They will then be automatically removed whenever tmp is deleted.
#
# ----------------------------------------------------------------------------

DL_DIR_pn-breakpad-wrapper              = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-cm-agent                 = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-common-library           = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-common-library-native    = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-cr                       = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-dmcli                    = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-epon-agent               = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-eth-agent                = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-gwprovapp                = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-gwprovapp-epon           = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-gwprovapp-ethwan         = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-home-security            = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-hotspot                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-hotspot-kmod             = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-lm-lite                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-logagent                 = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-misc                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-moca                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-mta-agent                = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-p-and-m                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-psm                      = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-snmp-pa                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-tr069-pa                 = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-wifi-agent               = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-xdns                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-dca                           = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-dcmjsonparser                 = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-cm-generic                = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-dhcpv4c-generic           = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-epon-generic              = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-ethsw-generic             = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-firewall-generic          = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-moca-generic              = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-mso-mgmt-generic          = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-mta-generic               = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-platform-generic          = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-vlan-generic              = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-wifi-generic              = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-halinterface                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-libsyswrapper                 = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-libunpriv                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-notify-comp                   = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rbus                          = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rbus-core                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rdk-logger                    = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rdklist                       = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rtmessage                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-sysint-broadband              = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-syslog-helper                 = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-telemetry                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-test-and-diagnostic           = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-utopia                        = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"

# ----------------------------------------------------------------------------
