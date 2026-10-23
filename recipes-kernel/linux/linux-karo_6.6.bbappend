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
