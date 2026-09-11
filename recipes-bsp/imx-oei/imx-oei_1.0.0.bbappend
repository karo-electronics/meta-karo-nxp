FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:${THISDIR}/${PN}/patches:"

SRC_URI += "file://0001-boards-add-support-for-qs95-5610-module.patch"
SRC_URI += "file://0002-boards-qs95-5610-derate-the-DDR-AC-timings-above-85-.patch"
