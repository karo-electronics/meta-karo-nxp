# Copyright (C)2019 Markus Bauer <mb@karo-electronics.de>
SUMMARY = "Appended NXP i.MX Kernel for support of Ka-Ro electronics TX CoM family."

# Ka-Ro specific patch set for NXP's linux-imx 4.14.98 2.0.0 ga
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-karo-4.14.98:"
SRC_URI_append = "\
	    file://0001-drm-panel-simple-Add-support-for-Tianma-TM101JVHG32-.patch \
	    file://0002-ARM64-dts-add-support-for-Ka-Ro-electronics-TX8M-161.patch \
	    file://0003-mfd-bd71837-select-missing-features-for-BD71837-PMIC.patch \
	    file://0004-regulator-bd71837-prevent-warning-when-compiled-with.patch \
	    file://0005-soc-imx-select-missing-PM_GENERIC_DOMAINS-for-i.MX8-.patch \
	    file://0006-ARM64-dts-imx8mm-tx8m-1610-disable-arm-idle.patch \
      	    ${@bb.utils.contains('KERNEL_FEATURES',"wifi","file://0007-karo-tx8m-enable-PCIe-support-for-LM511-WLAN-module.patch","",d)} \
            file://0008-ARM64-dts-imx8mm-add-missing-bus-range-property-to-p.patch \
	    file://0009-enable-display-with-downgrading-sec-dsim.patch \
	    file://0010-add-dtb-for-using-can-spi-mcp251x-device.patch \
	    file://0011-patch-for-edt-m12.patch \
	    file://0012-karo-dtbs.patch \
	    file://0013-busformat-override.patch \
	    file://0014-display-support.patch \
	    file://0015-adding-txul-and-tx6-defconfings.patch \
"

KBUILD_DEFCONFIG_tx8m = "tx8m_defconfig"
KBUILD_DEFCONFIG_txul = "txul_defconfig"
KBUILD_DEFCONFIG_tx6 = "tx6_defconfig"

addtask copy_defconfig after do_patch before do_preconfigure
do_copy_defconfig () {
    install -d ${B}
    if [ ${IS_MODULE_TXUL} = "yes" ]; then
        # copy txul_defconfig to use for mx6ul
        mkdir -p ${B}
        cp ${S}/arch/arm/configs/txul_defconfig ${B}/.config
        cp ${S}/arch/arm/configs/txul_defconfig ${B}/../defconfig
    elif [ ${IS_MODULE_TX6} = "yes" ]; then
        # copy latest defconfig to use for mx6
        mkdir -p ${B}
        cp ${S}/arch/arm/configs/tx6_defconfig ${B}/.config
        cp ${S}/arch/arm/configs/tx6_defconfig ${B}/../defconfig
    elif [ ${IS_MODULE_TX8} = "yes" ]; then
        # copy latest defconfig to use for tx8m
        mkdir -p ${B}
	cp ${S}/arch/arm64/configs/${@bb.utils.contains('KERNEL_FEATURES',"qt5","defconfig","tx8m_defconfig",d)} ${B}/.config
	cp ${S}/arch/arm64/configs/${@bb.utils.contains('KERNEL_FEATURES',"qt5","defconfig","tx8m_defconfig",d)} ${B}/../defconfig
    fi
}
