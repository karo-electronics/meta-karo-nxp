FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}:${THISDIR}/${BP}/patches:${THISDIR}/${BP}/env:"
SRC_URI:append = " \
    file://karo.bmp;subdir=git/tools/logos \
"

SRC_URI:append:mx9-nxp-bsp = " \
    file://dts/imx93-karo-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/imx93-karo.dtsi;subdir=git/arch/arm \
"

SRC_URI:append:mx8m-nxp-bsp = " \
    file://dts/imx8m-karo-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/imx8m-karo.dtsi;subdir=git/arch/arm \
    file://dts/imx8m-qs8m-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/imx8m-qs8m.dtsi;subdir=git/arch/arm \
    file://dts/imx8mm-karo-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/imx8mm-karo.dtsi;subdir=git/arch/arm \
    file://dts/imx8mm-tx8m-16xx-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/imx8mm-tx8m-16xx.dtsi;subdir=git/arch/arm \
    file://dts/imx8mm-tx8m-mipi-mb.dtsi;subdir=git/arch/arm \
    file://dts/imx8mn-karo-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/imx8mn-karo.dtsi;subdir=git/arch/arm \
    file://dts/imx8mn-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/imx8mp-karo-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/imx8mp-karo.dtsi;subdir=git/arch/arm \
    file://dts/imx8mp-tx8p-lvds-mb.dtsi;subdir=git/arch/arm \
"
SRC_URI:append:tx8p-ml82 = " \
    file://dts/imx8mp-tx8p-ml81-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/imx8mp-tx8p-ml81.dts;subdir=git/arch/arm \
    file://dts/imx8mp-tx8p-ml81-lvds-mb-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/imx8mp-tx8p-ml81-lvds-mb.dts;subdir=git/arch/arm \
"

UBOOT_FEATURES:append = "${@ bb.utils.contains('DISTRO_FEATURES', "copro", " copro", "", d)}"
UBOOT_FEATURES:append = " fastboot"
