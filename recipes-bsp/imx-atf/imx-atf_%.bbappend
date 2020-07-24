SRCBRANCH = "imx_4.14.98_2.3.0"
SRCREV = "bb209a0b4ccca2aa4a3a887f9606dc4a3d294adf"

FILESEXTRAPATHS_prepend := "${THISDIR}/atf:"
SRC_URI_append = " \
	file://0001-imx8m-use-SRS-to-reset-board.patch \
"
