MACHINEOVERRIDES =. "mx8:mx8m:mx8mm:tx8m:"

KBUILD_BUILD_USER := "support"
KBUILD_BUILD_HOST := "karo-electronics.de"

require conf/machine/include/imx-base.inc
require conf/machine/include/tune-cortexa53.inc

MACHINE_FEATURES += " wifi"

MACHINE_USES_VIVANTE_KERNEL_DRIVER_MODULE = "0"
MACHINE_HAS_VIVANTE_KERNEL_DRIVER_SUPPORT = "1"

UBOOT_CONFIG ??= "default"

SPL_BINARY = "spl/u-boot-spl.bin"

# Set DDR FIRMWARE
DDR_FIRMWARE_NAME = "ddr3_imem_1d.bin ddr3_dmem_1d.bin"

# Set imx-mkimage boot target
IMXBOOT_TARGETS = "flash_tx8m"

# Set Serial console
SERIAL_CONSOLES = "115200;ttymxc0"

IMAGE_BOOTLOADER = "imx-boot"

LOADADDR = ""
UBOOT_SUFFIX = "bin"
UBOOT_MAKE_TARGET = ""

# Kernel Configs
PREFERRED_VERSION_linux-imx ?= "5.4"

# Remove some image types we don't need
IMAGE_FSTYPES_remove = "sdcard"

DL_DIR ?= "${BSPDIR}/downloads/"

PREFERRED_PROVIDER_ptpd = "ptpd"
# Use correct VPU
PREFERRED_PROVIDER_virtual/imxvpu = "imx-vpu-hantro"
