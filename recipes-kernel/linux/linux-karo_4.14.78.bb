# Copyright (C)2019 Oliver Wendt <OW@KARO-electronics.de>
SUMMARY = "Linux Kernel provided and supported by NXP inc. Ka-Ro patchset"
DESCRIPTION = "Linux Kernel provided and supported by NXP with focus on \
i.MX Family Reference Boards. It includes support for many IPs such as GPU, VPU and IPU. \
This subversion also includes a set of patches for the Ka-Ro TX8M solution."

require recipes-kernel/linux/linux-imx.inc
require recipes-kernel/linux/linux-imx-src-${PV}.inc

# Ka-Ro specific patch set for NXP's linux-imx 4.14.78 1.0.0 ga
SRC_URI = " ${KERNEL_SRC};branch=${SRCBRANCH} \
	    file://0001-drm-panel-simple-Add-support-for-Tianma-TM101JVHG32-.patch \
	    file://0002-ARM64-dts-add-support-for-Ka-Ro-electronics-TX8M-161.patch \
	    file://0003-mfd-bd71837-select-missing-features-for-BD71837-PMIC.patch \
	    file://0004-regulator-bd71837-prevent-warning-when-compiled-with.patch \
	    file://0005-soc-imx-select-missing-PM_GENERIC_DOMAINS-for-i.MX8-.patch \
	    file://0006-ARM64-dts-imx8mm-tx8m-1610-disable-arm-idle.patch \
            ${@bb.utils.contains('KERNEL_FEATURES',"wifi","file://0007-karo-tx8m-enable-PCIe-support-for-LM511-WLAN-module.patch","",d)} \
            file://0008-ARM64-dts-imx8mm-add-missing-bus-range-property-to-p.patch \
"

# Ka-Ro specific Device Tree files to be compiled
KERNEL_DEVICETREE_append_tx8m-1610 = " freescale/imx8mm-tx8m-1610.dtb \
                             freescale/imx8mm-tx8m-1610-mipi-mb.dtb \
"

UBOOT_LOADADDRESS = "0x40480000"
KERNEL_IMAGETYPE = "uImage"

#KERNEL_DEVICETREE ?= "${DEFAULT_DEVICETREE}"

KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES',"wifi"," wifi","",d)}"

# Ka-Ro specific Kernel config to be used for compiling
KERNEL_DEFCONFIG_tx8m-1610 = "${KERNEL_SRC}/arch/arm64/configs/tx8m_defconfig"

COMPATIBLE_MACHINE = "(tx8m*)"

addtask copy_defconfig after do_unpack before do_preconfigure
do_copy_defconfig () {
    install -d ${B}
    # copy latest defconfig to use for tx8m
    mkdir -p ${B}
    cp ${S}/arch/arm64/configs/tx8m_defconfig ${B}/.config
    cp ${S}/arch/arm64/configs/tx8m_defconfig ${B}/../defconfig
}
