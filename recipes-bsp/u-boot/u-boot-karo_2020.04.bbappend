FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}:${THISDIR}/${BP}/patches:${THISDIR}/${BP}/env:"
SRC_URI:append = " \
	file://karo.bmp;subdir=git/tools/logos \
"

UBOOT_ENV_FILE ?= "${@ "${MACHINE}%s_env.txt" % ("-${KARO_BASEBOARD}" if "${KARO_BASEBOARD}" != "" else "")}"

SRC_URI:append = " \
	${@ "file://${UBOOT_ENV_FILE};subdir=git/board/karo/tx8m" if "${UBOOT_ENV_FILE}" != "" else ""} \
	file://dts/imx8m-tx8m-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-karo.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-qs8m-mq00-qsbase2-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-qs8m-mq00-qsbase2.dts;subdir=git/arch/arm \
	file://dts/imx8mm-qs8m-mq00-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-qs8m-mq00.dts;subdir=git/arch/arm \
	file://dts/imx8mm-qsxm-mm60-qsbase3-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-qsxm-mm60-qsbase3.dts;subdir=git/arch/arm \
	file://dts/imx8mm-qsxm-mm60-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-qsxm-mm60.dts;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1610-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1610.dts;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1620-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1620.dts;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1622-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1622.dts;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-16xx.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-mipi-mb.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-tx8mm-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mn-karo.dtsi;subdir=git/arch/arm \
	file://dts/imx8mn-qs8m-nd00-qsbase2-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mn-qs8m-nd00-qsbase2.dts;subdir=git/arch/arm \
	file://dts/imx8mn-qs8m-nd00-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mn-qs8m-nd00.dts;subdir=git/arch/arm \
	file://dts/imx8mn-tx8m-nd00-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mn-tx8m-nd00.dts;subdir=git/arch/arm \
	file://dts/imx8mn-tx8mn-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-karo.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-qsxp-ml81-qsbase3-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-qsxp-ml81-qsbase3.dts;subdir=git/arch/arm \
	file://dts/imx8mp-qsxp-ml81-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-qsxp-ml81.dts;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-lvds-mb.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml81-lvds-mb-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml81-lvds-mb.dts;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml81-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml81.dts;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml82-lvds-mb-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml82-lvds-mb.dts;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml82-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml82.dts;subdir=git/arch/arm \
"
