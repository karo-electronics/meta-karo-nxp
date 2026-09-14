FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}/patches:"

SRC_URI:append = " \
    file://karo-spidev-test.patch \
"

SRC_URI:append = " \
    file://dts/freescale/imx8m-qs8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx8m-tx8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx8mm-tx8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx8mp-karo.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx93-karo.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx91-karo.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
"
