KBUILD_BUILD_USER := "support"
KBUILD_BUILD_HOST := "karo-electronics.de"

#IMX_DEFAULT_BOOTLOADER ?= "u-boot-karo"

MACHINEOVERRIDES =. "mx8:mx8m:tx8m:"

require conf/machine/include/imx-base.inc
require conf/machine/include/tune-cortexa53.inc

MACHINE_FEATURES += "emmc"

VIRTUAL-RUNTIME_dev_manager ??= "udev"
VIRTUAL-RUNTIME_init_manager ?= "${@bb.utils.contains('DISTRO_FEATURES',"systemd","systemd","sysvinit",d)}"

MACHINE_USES_VIVANTE_KERNEL_DRIVER_MODULE = "0"
MACHINE_HAS_VIVANTE_KERNEL_DRIVER_SUPPORT = "1"

UBOOT_CONFIG ??= "noenv mfg default"

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

PREFERRED_VERSION_linux-karo ?= "5.4"

IMAGE_FSTYPES_append = " ext4"

# Remove some image types we don't need
IMAGE_FSTYPES_remove = "sdcard.bz2"

PREFERRED_PROVIDER_ptpd = "ptpd"

# Use correct VPU
#PREFERRED_PROVIDER_virtual/imxvpu = "imx-vpu-hantro"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS += "firmware-imx-sdma"

# Blacklist evbug kernel module
KERNEL_MODULE_PROBECONF += "evbug"
module_conf_evbug = "blacklist evbug"

FILESYSTEM_PERMS_TABLES = "files/fs-perms-karo.txt"
