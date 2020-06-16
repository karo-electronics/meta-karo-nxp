# Copyright (C)2019 Markus Bauer <mb@karo-electronics.de>
SUMMARY = "Appended NXP i.MX Kernel for support of Ka-Ro electronics TX CoM family."

# Ka-Ro specific patch set for NXP's linux-imx 5.4
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-karo-5.4/patches:${THISDIR}/linux-karo-5.4:"
SRC_URI_append = "\
	file://0001-TI-SN65DSI83-bridge-driver-support.patch \
	file://0002-Little-fixes-for-imx-drm-drivers.patch \
	file://0003-Add-RaspberryPi-7inch-touchscreen-display-support-dr.patch \
"

SRC_URI_append_mx8 = "\
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-dsi83.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-raspi-display.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-16x0.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1610-mipi-mb.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1610-tx4etml0500.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1610.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1620-lvds-mb.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1620.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-lvds-mb.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-mipi-mb.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-tx8m-mipi-mb.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-tx8m-nd00-mipi-mb.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-tx8m-nd00.dts;subdir=git/arch/arm64/boot \
	file://qs8m-mq00_defconfig;subdir=git/arch/arm64/configs \
	file://tx8m-16x0_defconfig;subdir=git/arch/arm64/configs \
	file://tx8m-nd00_defconfig;subdir=git/arch/arm64/configs \
"

KBUILD_DEFCONFIG_tx8m-1610 = "tx8m-16x0_defconfig"
KBUILD_DEFCONFIG_tx8m-1620 = "tx8m-16x0_defconfig"
KBUILD_DEFCONFIG_qs8m-mq00 = "qs8m-mq00_defconfig"
KBUILD_DEFCONFIG_tx8m-nd00 = "tx8m-nd00_defconfig"

DEFCONFIG_PATH_mx8 = "arch/arm64/configs"

do_copy_defconfig () {
    	install -d ${B}
    	mkdir -p ${B}
    	cp ${S}/${DEFCONFIG_PATH}/${KBUILD_DEFCONFIG} ${B}/.config
    	cp ${S}/${DEFCONFIG_PATH}/${KBUILD_DEFCONFIG} ${B}/../defconfig
}
addtask copy_defconfig after do_unpack before do_preconfigure
