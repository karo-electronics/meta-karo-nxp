FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-2.2:"

ATF_SRC = "git://github.com/karo-electronics/imx-atf.git;protocol=https"
SRCBRANCH = "lf_v2.4-karo"
SRCREV = "8bc6c402c1a080c99c065a16a331f814d415b83f"

EXTRA_OEMAKE += " \
        IMX_BOOT_UART_BASE="${IMX_BOOT_UART_BASE}" \
"
