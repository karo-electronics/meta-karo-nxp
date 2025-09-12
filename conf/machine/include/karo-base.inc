KBUILD_BUILD_USER := "support"
KBUILD_BUILD_HOST := "karo-electronics.de"

BUILD_REPRODUCIBLE_BINARIES ?= "0"
INHERIT:remove = "${@'reproducible_build' if d.getVar('BUILD_REPRODUCIBLE_BINARIES') == '0' else ''}"

require conf/machine/include/imx-base.inc

# override unconditional assignment in meta-imx/.../imx-base-extend.inc
QEMUVERSION = "8.2.7"

MACHINE_FEATURES += "emmc"

MACHINE_HAS_VIVANTE_KERNEL_DRIVER_SUPPORT = "0"

SOC_FAMILY ??= "${SOC_PREFIX}"
MACHINEOVERRIDES =. "${@ "${SOC_FAMILY}:" if "${SOC_FAMILY}" != "${SOC_PREFIX}" else ""}"

UBOOT_CONFIG ??= "default mfg noenv"
UBOOT_FEATURES ??= ""
UBOOT_FEATURES:append = "${@ " pmic-${KARO_BOARD_PMIC}" if "${KARO_BOARD_PMIC}" != "" else ""}"
UBOOT_FEATURES:append = "${@ " optee" if 'optee' in d.getVar('MACHINE_FEATURES').split() else ""}"

SOC_DEFAULT_IMAGE_FSTYPES = "tar.bz2"

# Set U-Boot DTB
KARO_BASEBOARD ??= ""
DTB_BASENAME ?= "${SOC_FAMILY}-${MACHINE}"
U_BOOT_DTB_NAME ?= "${@ "${DTB_BASENAME}%s" % ("-${KARO_BASEBOARD}" if "${KARO_BASEBOARD}" != "" else "")}"

KERNEL_DEVICETREE += "freescale/${DTB_BASENAME}.dtb"

BOOT_TOOLS = "imx-boot-tools"

SPL_BINARY = "spl/u-boot-spl.bin"

# Set Serial console
# The override is required to overrule the unconditional 'SERIAL_CONSOLES = ...'
# setting in meta-freescale/conf/machine/include/imx-base.inc
SERIAL_CONSOLES:mx9-nxp-bsp ?= "115200;ttyLP0 115200;ttyLP1 115200;ttyLP2"
SERIAL_CONSOLES:mx8-nxp-bsp ?= "115200;ttymxc0 115200;ttymxc1 115200;ttymxc2"

IMAGE_BOOTLOADER = "u-boot"

LOADADDR = ""
UBOOT_SUFFIX = "bin"
UBOOT_MAKE_TARGET = ""

IMX_DEFAULT_BSP = "nxp"
PREFERRED_PROVIDER_virtual/bootloader = "u-boot-karo"
PREFERRED_PROVIDER_virtual/kernel = "linux-karo"
PREFERRED_VERSION_linux-karo ?= "6.6"
PREFERRED_VERSION_uboot-karo ?= "2025.07"

PREFERRED_PROVIDER_ptpd = "ptpd"

# Blacklist evbug kernel module
KERNEL_MODULE_PROBECONF += "evbug"
module_conf_evbug = "blacklist evbug"

PREFERRED_VERSION_isp-imx                 = "4.2.2.24+basler"
PREFERRED_VERSION_basler-camera           = "4.2.2.24+basler"
PREFERRED_VERSION_kernel-module-isp-vvcam = "4.2.2.24+basler"

PREFERRED_PROVIDER_opencl-headers:imxgpu = "imx-gpu-viv"
PREFERRED_PROVIDER_opencl-icd-loader:imxgpu = "imx-gpu-viv"
PREFERRED_PROVIDER_virtual/libgles3:imxgpu3d = "imx-gpu-viv"

# Set a more generic tuning for code reuse across parts
# taken from imx-base.inc
DEFAULTTUNE:mx8m-nxp-bsp   ?= "armv8a-crc-crypto"
DEFAULTTUNE:mx9-nxp-bsp   ?= "armv8a-crc-crypto"

PACKAGECONFIG:append:pn-gstreamer1.0-plugins-ugly = " x264"
PACKAGECONFIG:append:pn-systemd = " unmanaged-network"
