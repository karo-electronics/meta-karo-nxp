# workaround for buggy NXP scarthgap-6.6.52 release:
# ERROR: firmware-nxp-wifi-1.0-r0 do_package: QA Issue: firmware-nxp-wifi-nxpiw610-sdio is listed in PACKAGES multiple times, this leads to packaging errors. [packages-list]
# duplicate entry 'firmware-nxp-wifi-nxpiw610-sdio' 'PACKAGES'
#
PACKAGES:remove = "${PN}-nxpiw610-sdio"
RDEPENDS:${PN}-all-sdio:remove = "${PN}-nxpiw610-sdio"

do_install:append() {
    rm -rvf ${D}${nonarch_base_libdir}/firmware/nxp/sd_iw610.bin.se \
            ${D}${nonarch_base_libdir}/firmware/nxp/sduart_iw610.bin.se \
            ${D}${nonarch_base_libdir}/firmware/nxp/uart_iw610_bt.bin.se \
            ${D}${nonarch_base_libdir}/firmware/nxp/uartspi_iw610.bin.se
}
