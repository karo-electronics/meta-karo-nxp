FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-2.2:"
SRC_URI_append = " \
	       file://self-refresh-errmsg.patch \
	       file://ddr3-bugfix.patch \
	       file://wdog-reset.patch \
	       file://debug-console.patch \
"

# downgrade version since imx8mp hangs with the current
SRCBRANCH = "imx_5.4.24_2.1.0"
SRCREV = "b0a00f22b09c13572d3e87902a1069dee34763bd"

