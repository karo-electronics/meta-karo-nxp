# Copyright (C)2019 Markus Bauer <mb@karo-electronics.de>
SUMMARY = "Appended NXP i.MX Kernel for support of Ka-Ro electronics TX CoM family."

# Ka-Ro specific patch set for NXP's linux-imx 4.14.98_2.2.0
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-karo-4.14.98:"
SRC_URI_append = "\
	file://0001-drm-panel-simple-Add-support-for-Tianma-TM101JVHG32-.patch \
    	file://0002-ARM64-dts-add-support-for-Ka-Ro-electronics-TX8M-161.patch \
    	file://0003-mfd-bd71837-select-missing-features-for-BD71837-PMIC.patch \
    	file://0004-regulator-bd71837-prevent-warning-when-compiled-with.patch \
    	file://0005-soc-imx-select-missing-PM_GENERIC_DOMAINS-for-i.MX8-.patch \
    	file://0006-ARM64-dts-imx8mm-tx8m-1610-disable-arm-idle.patch \
	file://0008-ARM64-dts-imx8mm-add-missing-bus-range-property-to-p.patch \
    	file://0010-add-dtb-for-using-can-spi-mcp251x-device.patch \
    	file://0011-patch-for-edt-m12.patch \
    	file://0012-karo-dtbs.patch \
    	file://0013-busformat-override.patch \
    	file://0014-display-support.patch \
    	file://0015-adding-txul-and-tx6-defconfings.patch \
    	file://0016-adding-defconfig-extension-for-wifi-and-systemd.patch \
    	file://0017-Revert-MLK-11117-01-ARM-clk-imx6-adjust-axi-clock-to.patch \
    	file://0018-ARM-imx-enable-OCOTP-clock-before-accessing-fuse-reg.patch \
    	file://0019-ARM-imx-enable-OCOTP-clock-before-accessing-ocotp-re.patch \
    	file://0020-thermal-imx_thermal-enable-OCOTP-clock-before-access.patch \
    	file://0021-tianma-add-2-lanes-version.patch \
	file://0023-split-defconfigs-into-nd00-and-1610-version.patch \
	file://0024-ARM64-DTS-Add-TX8M-ND00-support.patch \
	file://0025-Add-SN65DSI83-MIPI2LVDS-Bridge-driver.patch \
	file://0026-ARM-DTS-fix-txul-8013-mainboard-dts.patch \
	file://0027-ARM64-CONFIGS-MXC-GPU-VIV-driver-also-for-tx8m-nd00.patch \
	file://0028-arm-imx6-fix-compile-error-when-CONFIG_ARM_PSCI_FW-i.patch \
	file://0029-arm-dts-imx6ul-consistently-use-can-12-instead-of-fl.patch \
	file://0030-arm-dts-imx6ul-remove-obsolete-imx6ul-tx6ul-mainboar.patch \
	file://0031-arm-dts-imx6ull-add-imx6ull-txul-8013-files-to-Makef.patch \
	file://0032-arm64-dts-add-tx8m-1620-support-beta-version.patch \
	file://0033-arm-dts-bugfix-reset-imx6ull-dtsi-to-mainline.patch \
	file://0034-drm-mxsfb-fix-possible-DEADLOCK-in-mxsfb-irq-handler.patch \
	file://0035-modify-tx8m-1610-and-nd00-defconfig.patch \
	file://0036-add-qs8m-mq00-on-qsbase2-support.patch \
"

KBUILD_DEFCONFIG_tx8m-1610 = "tx8m-1610_defconfig"
KBUILD_DEFCONFIG_tx8m-1620 = "tx8m-1610_defconfig"
KBUILD_DEFCONFIG_qs8m-mq00 = "tx8m-1610_defconfig"
KBUILD_DEFCONFIG_tx8m-nd00 = "tx8m-nd00_defconfig"
KBUILD_DEFCONFIG_mx6ul = "txul_defconfig"
KBUILD_DEFCONFIG_mx6 = "tx6_defconfig"

DEFCONFIG_PATH = "arch/arm/configs"
DEFCONFIG_PATH_tx8m = "arch/arm64/configs"

do_copy_defconfig () {
    	install -d ${B}
    	mkdir -p ${B}
    	cp ${S}/${DEFCONFIG_PATH}/${KBUILD_DEFCONFIG} ${B}/.config
    	cp ${S}/${DEFCONFIG_PATH}/${KBUILD_DEFCONFIG} ${B}/../defconfig
    	if [ ${@bb.utils.contains('KERNEL_FEATURES',"wifi","yes","no",d)} = "yes" ]; then
		cat ${S}/arch/arm/configs/wifi.cfg >> ${B}/.config
		cat ${S}/arch/arm/configs/wifi.cfg >> ${B}/../defconfig
    	fi
    	if [ ${@bb.utils.contains('KERNEL_FEATURES',"systemd","yes","no",d)} = "yes" ]; then
		cat ${S}/arch/arm/configs/systemd.cfg >> ${B}/.config
		cat ${S}/arch/arm/configs/systemd.cfg >> ${B}/../defconfig
    	fi
}
addtask copy_defconfig after do_patch before do_preconfigure
