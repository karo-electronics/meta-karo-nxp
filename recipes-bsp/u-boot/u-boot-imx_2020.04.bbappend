FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:${THISDIR}/${PN}-${PV}/patches:"
SRC_URI_append = " \
	file://karo.bmp;subdir=git/tools/logos \
"
