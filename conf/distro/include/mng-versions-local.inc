
# ----------------------------------------------------------------------------

RDK_RELEASE ?= "8888"

LGI_RDKB_GIT ?= "git://${@os.path.normpath("${COREBASE}/../src")}"
LGI_RDKB_GIT_SUFFIX ?= ""
LGI_RDKB_GIT_PROTOCOL ?= "file"
LGI_RDKB_GIT_EXTRAOPT ?= ";usehead=1"

# ----------------------------------------------------------------------------

# In most cases the UI repos will come from the same place as the RDKB repos,
# but use separate definitions to handle corner cases where they do not.

LGI_UI_GIT ?= "git://${@os.path.normpath("${COREBASE}/../src")}"
LGI_UI_GIT_SUFFIX ?= ""
LGI_UI_GIT_PROTOCOL ?= "file"
LGI_UI_GIT_EXTRAOPT ?= ";usehead=1"

WEBUI_OFW_REPONAME ?= "webui-ofw"

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
DL_DIR_pn-ccsp-jse                      = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-lm-lite                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-misc                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-moca                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-mta-agent                = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-p-and-m                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-psm                      = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-snmp-pa                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-tr069-pa                 = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-wifi-agent               = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ccsp-xdns                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-core-net-lib                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-dca                           = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-dcmjsonparser                 = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-cm-generic                = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-dhcpv4c-generic           = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-epon-generic              = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-ethsw-generic             = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-fwupgrade-generic         = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-gwprovappabs-generic      = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-moca-generic              = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-mso-mgmt-generic          = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-mta-generic               = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-platform-generic          = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-vlan-generic              = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-hal-wifi-generic              = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-halinterface                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-ipoe-health-check             = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-json-hal-lib                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-libsyswrapper                 = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-libunpriv                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-mesh-agent                    = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-notify-comp                   = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rbus                          = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rdk-fwupgrade-manager         = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rdk-logger                    = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rdk-vlanmanager               = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rdk-wanmanager                = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rdkgponmanager                = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rdklist                       = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-rdktelcovoicemanager          = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-sysint-broadband              = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-syslog-helper                 = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-telemetry                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-test-and-diagnostic           = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-utopia                        = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-webconfig-framework           = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"

DL_DIR_pn-rest-backend                  = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"
DL_DIR_pn-webui-ofw                     = "${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}/${PN}/downloads"

# ----------------------------------------------------------------------------
