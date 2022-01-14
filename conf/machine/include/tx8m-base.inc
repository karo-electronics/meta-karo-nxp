KBUILD_BUILD_USER := "support"
KBUILD_BUILD_HOST := "karo-electronics.de"

MACHINEOVERRIDES =. "mx8:mx8m:tx8m:"

BUILD_REPRODUCIBLE_BINARIES ?= "0"
INHERIT_remove = "${@'reproducible_build' if d.getVar('BUILD_REPRODUCIBLE_BINARIES') == '0' else ''}"

require conf/machine/include/imx-base.inc
require conf/machine/include/tune-cortexa53.inc

MACHINE_FEATURES += "emmc"
MACHINE_FEATURES_remove = "nxp8987"

MACHINE_USES_VIVANTE_KERNEL_DRIVER_MODULE = "0"
MACHINE_HAS_VIVANTE_KERNEL_DRIVER_SUPPORT = "1"

KARO_BOARD_PMIC ?= "bd71847"

UBOOT_CONFIG ??= "noenv mfg default"

SPL_BINARY = "spl/u-boot-spl.bin"

# Set imx-mkimage boot target
IMXBOOT_TARGETS ?= "flash_tx8m"
IMX_BOOT_SOC_TARGET ?= "iMX8MM"
ATF_PLATFORM ?= "imx8mm"
ATF_LOAD_ADDR ?= "0x920000"

# Set Serial console
SERIAL_CONSOLES = "115200;ttymxc0 115200;ttymxc1 115200;ttymxc2"
# for imx-atf
IMX_BOOT_UART_BASE = "0x30860000"

# Set DDR FIRMWARE
DDR_FIRMWARE_NAME ?= " \
        ddr3_imem_1d.bin \
        ddr3_dmem_1d.bin \
"

IMAGE_BOOTLOADER = "imx-boot"

LOADADDR = ""
UBOOT_SUFFIX = "bin"
UBOOT_MAKE_TARGET = ""

IMX_DEFAULT_BSP = "nxp"
PREFERRED_PROVIDER_virtual/bootloader_imx = "u-boot-imx"
PREFERRED_PROVIDER_virtual/kernel_imx = "linux-imx"

PREFERRED_PROVIDER_ptpd = "ptpd"

# Blacklist evbug kernel module
KERNEL_MODULE_PROBECONF += "evbug"
module_conf_evbug = "blacklist evbug"

PREFERRED_VERSION_isp-imx                 = "4.2.2.14+basler"
PREFERRED_VERSION_basler-camera           = "4.2.2.14+basler"
PREFERRED_VERSION_kernel-module-isp-vvcam = "4.2.2.14+basler"

IMX_GPU_VERSION            ?= "6.4.3.p2.4"
IMX_GPU_VERSION_SUFFIX      = "aarch64"

PREFERRED_VERSION_imx-gpu-viv               = "${IMX_GPU_VERSION}-${IMX_GPU_VERSION_SUFFIX}"
PREFERRED_VERSION_kernel-module-imx-gpu-viv = "${IMX_GPU_VERSION}"
PREFERRED_VERSION_imx-gpu-g2d               = "${IMX_GPU_VERSION}"

# Set the preffered provider for opencl-headers
PREFERRED_PROVIDER_opencl-headers_imx = "imx-gpu-viv"

PREFERRED_VERSION_gstreamer1.0-plugins-base_mx8 = "1.18.5.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-bad_mx8  = "1.18.5.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-good_mx8 = "1.18.5.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-good-qt_mx8 = "1.18.5.imx"
PREFERRED_VERSION_gstreamer1.0_mx8              = "1.18.5.imx"
PREFERRED_VERSION_gstreamer1.0-libav_mx8        = "1.18.5"
