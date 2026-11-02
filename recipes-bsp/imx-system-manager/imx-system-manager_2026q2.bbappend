FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:${THISDIR}/${PN}/patches:"

SRC_URI += "file://0001-boards-krimx95-add-board-support-for-the-QS95-5610-m.patch"
SRC_URI += "file://0002-configs-krimx95-add-SM-configuration-for-the-QS95-56.patch"
