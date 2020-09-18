FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
SRC_URI_append = " \
	file://karo.bmp;subdir=git/tools/logos \
"
