# Copyright (C)2019 Markus Bauer <mb@karo-electronics.de>
SUMMARY = "Appended NXP i.MX Kernel for support of Ka-Ro electronics TX8M."

# Ka-Ro specific patch set for NXP's linux-imx 4.14.78 1.0.0 ga
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-karo-4.14.78:"
SRC_URI_append = "\
	    file://0001-drm-panel-simple-Add-support-for-Tianma-TM101JVHG32-.patch \
	    file://0002-ARM64-dts-add-support-for-Ka-Ro-electronics-TX8M-161.patch \
	    file://0003-mfd-bd71837-select-missing-features-for-BD71837-PMIC.patch \
	    file://0004-regulator-bd71837-prevent-warning-when-compiled-with.patch \
	    file://0005-soc-imx-select-missing-PM_GENERIC_DOMAINS-for-i.MX8-.patch \
	    file://0006-ARM64-dts-imx8mm-tx8m-1610-disable-arm-idle.patch \
      ${@bb.utils.contains('KERNEL_FEATURES',"wifi","file://0007-karo-tx8m-enable-PCIe-support-for-LM511-WLAN-module.patch","",d)} \
      file://0008-ARM64-dts-imx8mm-add-missing-bus-range-property-to-p.patch \
"
