MACHINEOVERRIDES =. "mx95:"

require conf/machine/include/imx-base.inc
require conf/machine/include/arm/armv8-2a/tune-cortexa55.inc

SOC_PREFIX = "imx95"
SOC_FAMILY ??= "${SOC_PREFIX}"

MACHINE_FEATURES += "pci optee"

# Set U-Boot DTB
KARO_BASEBOARD ??= ""
KERNEL_DEVICETREE_BASENAME ?= "${SOC_FAMILY}-${MACHINE}"
UBOOT_DTB_NAME ?= "${@ "${KERNEL_DEVICETREE_BASENAME}%s" % ("-${KARO_BASEBOARD}" if "${KARO_BASEBOARD}" != "" else "")}"
UBOOT_ENV_FILE ?= "${@ "${MACHINE}%s" % ("-${KARO_BASEBOARD}" if "${KARO_BASEBOARD}" != "" else "")}"

KERNEL_DEVICETREE = " \
    freescale/${KERNEL_DEVICETREE_BASENAME}.dtb \
"

IMX_DEFAULT_BOOTLOADER:use-nxp-bsp = "u-boot-imx"
IMX_DEFAULT_BOOTLOADER:use-mainline-bsp = "u-boot-fslc"

LOADADDR = ""
UBOOT_SUFFIX = "bin"
UBOOT_MAKE_TARGET = ""

SPL_BINARY = "spl/u-boot-spl.bin"

UBOOT_CONFIG ??= "default mfg noenv"

ATF_PLATFORM = "imx95"
OEI_CORE   = "m33"
OEI_SOC    = "mx95"
OEI_BOARD  ?= "mx95lp4x-15"
DDR_TYPE   ?= "lpddr4"

OEI_DDR_CONFIG_ECC = ""

IMXBOOT_VARIANTS = ""

SYSTEM_MANAGER_CONFIG = "tx95"

IMXBOOT_TARGETS = "${IMXBOOT_TARGETS_BASENAME}_all ${IMXBOOT_TARGETS_BASENAME}_a55"

IMX_BOOT_SOC_TARGET = "iMX95"
IMX_BOOT_SEEK = "32"

SERIAL_CONSOLES = "115200;ttyLP0 115200;ttyLP1 115200;ttyLP2"

IMX_DEFAULT_BSP = "nxp"

SOC_DEFAULT_WKS_FILE:mx9-generic-bsp ?= "karo-imx-boot.wks.in"
