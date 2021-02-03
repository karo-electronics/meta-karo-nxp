KBUILD_BUILD_USER := "support"
KBUILD_BUILD_HOST := "karo-electronics.de"

#IMX_DEFAULT_BOOTLOADER ?= "u-boot-karo"

MACHINEOVERRIDES =. "mx8:mx8m:tx8m:"

require conf/machine/include/imx-base.inc
require conf/machine/include/tune-cortexa53.inc

MACHINE_FEATURES += "emmc"

MACHINE_USES_VIVANTE_KERNEL_DRIVER_MODULE = "0"
MACHINE_HAS_VIVANTE_KERNEL_DRIVER_SUPPORT = "1"

KARO_BOARD_PMIC ??= "bd71847"

UBOOT_CONFIG ??= "noenv mfg default"

SPL_BINARY = "spl/u-boot-spl.bin"

# Set DDR FIRMWARE
DDR_FIRMWARE_NAME ?= "ddr3_imem_1d.bin ddr3_dmem_1d.bin"

# Set imx-mkimage boot target
IMXBOOT_TARGETS ?= "flash_tx8m"

# Set Serial console
SERIAL_CONSOLES = "115200;ttymxc0 115200;ttymxc1 115200;ttymxc2"

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

PREFERRED_VERSION_isp-imx                 = "4.2.2.8+basler"
PREFERRED_VERSION_basler-camera           = "4.2.2.8+basler"
PREFERRED_VERSION_kernel-module-isp-vvcam = "4.2.2.8+basler"

IMX_GPU_VERSION            ?= "6.4.3.p1.0"
IMX_GPU_VERSION_SUFFIX      = "aarch64"

PREFERRED_VERSION_imx-gpu-viv               = "${IMX_GPU_VERSION}-${IMX_GPU_VERSION_SUFFIX}"
PREFERRED_VERSION_kernel-module-imx-gpu-viv = "${IMX_GPU_VERSION}"
PREFERRED_VERSION_imx-gpu-g2d               = "${IMX_GPU_VERSION}"

# Set the preffered provider for opencl-headers
PREFERRED_PROVIDER_opencl-headers_imx = "imx-gpu-viv"

PREFERRED_VERSION_gstreamer1.0-plugins-base = "1.16.2.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-bad  = "1.16.2.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-good = "1.16.2.imx"
PREFERRED_VERSION_gstreamer1.0              = "1.16.2.imx"
PREFERRED_VERSION_gstreamer1.0-libav        = "1.16.2"
