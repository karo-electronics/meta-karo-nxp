require conf/machine/include/imx-base.inc

MACHINE_FEATURES:append = " emmc"

SOC_FAMILY ??= "${SOC_PREFIX}"

# Set U-Boot DTB
KARO_BASEBOARD ??= ""
DTB_BASENAME ?= "${SOC_FAMILY}-${MACHINE}"
UBOOT_DTB_NAME ?= "${@ "${DTB_BASENAME}%s" % ("-${KARO_BASEBOARD}" if "${KARO_BASEBOARD}" != "" else "")}"
UBOOT_ENV_FILE ?= "${@ "${MACHINE}%s" % ("-${KARO_BASEBOARD}" if "${KARO_BASEBOARD}" != "" else "")}"

KERNEL_DEVICETREE = " \
    freescale/${DTB_BASENAME}.dtb \
"

IMX_DEFAULT_BSP = "nxp"
IMX_DEFAULT_BOOTLOADER:use-nxp-bsp = "u-boot-imx"
IMX_DEFAULT_BOOTLOADER:use-mainline-bsp = "u-boot-fslc"

LOADADDR = ""
UBOOT_SUFFIX = "bin"
UBOOT_MAKE_TARGET = ""

SPL_BINARY = "spl/u-boot-spl.bin"

UBOOT_CONFIG ??= "default mfg noenv"

UBOOT_FEATURES ??= ""

# for fw_printenv/fw_setenv
UBOOT_INITIAL_ENV ?= "u-boot-initial-env"

IMXBOOT_VARIANTS = ""

SOC_DEFAULT_WKS_FILE = "karo-image.wks.in"

# ttyLP1 = customer UART_B, ttyLP2 pads drive PCIe (QSBASE95)
SERIAL_CONSOLES = "115200;ttyLP0"
