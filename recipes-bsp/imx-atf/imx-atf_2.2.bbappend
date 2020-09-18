FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-2.2:"
SRC_URI_append = " \
	       file://self-refresh-errmsg.patch \
	       file://ddr3-bugfix.patch \
	       file://wdog-reset.patch \
	       file://debug-console.patch \
"
