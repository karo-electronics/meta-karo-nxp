FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}/patches:"

SRC_URI:append = " \
    file://0001-karo-spidev-test.patch \
    file://0002-soc-imx-src-fix-imx_src_is_m4_enabled-redefinition.patch \
"

SRC_URI:append = " \
    file://dts/freescale/imx8m-qs8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx8m-tx8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx8mm-tx8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx8mp-karo.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx93-karo.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
    file://dts/freescale/imx91-karo.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/nxp/imx/imx6qdl-tx6.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/nxp/imx/imx6qdl-tx6-lcd.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/nxp/imx/imx6qdl-tx6-lvds.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/nxp/imx/imx6qdl-tx6-mb7.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/nxp/imx/imx6qdl-tx6.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/nxp/imx/imx6qdl-tx6-lcd.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/nxp/imx/imx6qdl-tx6-lvds.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/nxp/imx/imx6qdl-tx6-mb7.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/nxp/imx/imx6ul-txul.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/nxp/imx/imx6ul-txul-mb7.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
"
