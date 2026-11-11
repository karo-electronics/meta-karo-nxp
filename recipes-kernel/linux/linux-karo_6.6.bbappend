FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}/patches:"

SRC_URI:append = " \
    file://karo-spidev-test.patch \
"

SRC_URI:append = " \
    file://dts/nxp/imx/imx6qdl-tx6.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/nxp/imx/imx6qdl-tx6-lcd.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/nxp/imx/imx6qdl-tx6-lvds.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/nxp/imx/imx6q-tx6q-1010.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/nxp/imx/imx6q-tx6q-1110.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/nxp/imx/imx6dl-tx6u-801x.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/nxp/imx/imx6dl-tx6u-811x.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/nxp/imx/imx6ul-txul.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx8m-qs8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx8m-tx8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx8mm-tx8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx8mp-karo.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx93-karo.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx91-karo.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
"

# i.MX6: SDMA probes before the rootfs is mounted, link its firmware into the kernel
DEPENDS:append:mx6-nxp-bsp = " firmware-imx"

do_configure:prepend:mx6-nxp-bsp() {
    install -D -m 0644 ${RECIPE_SYSROOT}${nonarch_base_libdir}/firmware/imx/sdma/sdma-imx6q.bin ${S}/firmware/imx/sdma/sdma-imx6q.bin
}
