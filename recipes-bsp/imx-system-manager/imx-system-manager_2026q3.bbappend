FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:${THISDIR}/${PN}/patches:"

SRC_URI += "file://0001-boards-qs95-add-board-support-for-the-QS95-9610-modu.patch"
SRC_URI += "file://0002-configs-qs95-add-SM-configuration-for-the-QS95-9610-.patch"
