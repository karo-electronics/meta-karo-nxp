FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:${THISDIR}/${PN}-${PV}/patches:"
SRC_URI_append = " \
	file://karo.bmp;subdir=git/tools/logos \
"
addtask do_configure before do_devshell
