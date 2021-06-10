SUMMARY = "WebUI dependencies"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "packagegroup-webui"

# The webUI package group is empty by default. The meta layer containing the
# webUI recipes should provide a .bbappend for the webUI package group to add
# the webUI packages.

RDEPENDS_packagegroup-webui = ""
