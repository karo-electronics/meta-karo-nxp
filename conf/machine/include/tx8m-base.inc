KBUILD_BUILD_USER := "support"
KBUILD_BUILD_HOST := "karo-electronics.de"

MACHINEOVERRIDES =. "mx8:mx8m:"

BUILD_REPRODUCIBLE_BINARIES ?= "0"
INHERIT:remove = "${@'reproducible_build' if d.getVar('BUILD_REPRODUCIBLE_BINARIES') == '0' else ''}"

require conf/machine/include/imx-base.inc
require conf/machine/include/arm/armv8a/tune-cortexa53.inc

SOC_PREFIX = "imx8m"

SOC_FAMILY ??= ""
SOC_FAMILY:mx8mm-nxp-bsp = "imx8mm"
SOC_FAMILY:mx8mn-nxp-bsp = "imx8mn"
SOC_FAMILY:mx8mp-nxp-bsp = "imx8mp"
MACHINEOVERRIDES =. "${@ "${SOC_FAMILY}:" if "${SOC_FAMILY}" != "" else ""}"

MACHINE_FEATURES += "emmc"
MACHINE_FEATURES:remove = "nxp8987"

MACHINE_USES_VIVANTE_KERNEL_DRIVER_MODULE = "0"
MACHINE_HAS_VIVANTE_KERNEL_DRIVER_SUPPORT = "1"

KARO_BOARD_PMIC ?= "bd71847"

UBOOT_CONFIG ??= "noenv mfg default"
UBOOT_FEATURES ??= ""
UBOOT_FEATURES:append = "${@ " pmic-${KARO_BOARD_PMIC}" if "${KARO_BOARD_PMIC}" != "" else ""}"

SOC_DEFAULT_IMAGE_FSTYPES = "tar.bz2"

# Set U-Boot DTB
KARO_BASEBOARD ??= ""
DTB_BASENAME ?= "${SOC_FAMILY}-${MACHINE}"
UBOOT_DTB_NAME ?= "${@ "${DTB_BASENAME}%s.dtb" % ("-${KARO_BASEBOARD}" if "${KARO_BASEBOARD}" != "" else "")}"

KERNEL_DEVICETREE:append = " freescale/${DTB_BASENAME}.dtb"

BOOT_TOOLS = "imx-boot-tools"

SPL_BINARY = "spl/u-boot-spl.bin"
UBOOT_INITIAL_ENV = ""

# Set imx-mkimage boot target
IMXBOOT_TARGETS ?= "flash_tx8m"
ATF_PLATFORM ?= "imx8mm"
ATF_LOAD_ADDR ?= "0x920000"

# Set Serial console
SERIAL_CONSOLES ?= "115200;ttymxc0 115200;ttymxc1 115200;ttymxc2"
# for imx-atf
IMX_BOOT_UART_BASE ??= "0x30860000"

# Set DDR FIRMWARE
DDR_FIRMWARE_NAME ?= " \
        ddr3_imem_1d.bin \
        ddr3_dmem_1d.bin \
"

IMAGE_BOOTLOADER = "u-boot"

LOADADDR = ""
UBOOT_SUFFIX = "bin"
UBOOT_MAKE_TARGET = ""

IMX_DEFAULT_BSP = "nxp"
PREFERRED_PROVIDER_virtual/bootloader = "u-boot-karo"
PREFERRED_PROVIDER_virtual/kernel = "linux-karo"
PREFERRED_VERSION_linux-karo ?= "6.1"

PREFERRED_PROVIDER_ptpd = "ptpd"

# Blacklist evbug kernel module
KERNEL_MODULE_PROBECONF += "evbug"
module_conf_evbug = "blacklist evbug"

# settings taken from fsl-imx-preferred-env.inc
PREFERRED_VERSION_gstreamer1.0-plugins-base = "1.20.3.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-bad  = "1.20.3.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-good = "1.20.3.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-good-qt = "1.20.3.imx"
PREFERRED_VERSION_gstreamer1.0              = "1.20.3.imx"

PREFERRED_VERSION_gstreamer1.0-plugins-base:mx8-nxp-bsp = "1.20.3.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-bad:mx8-nxp-bsp  = "1.20.3.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-good:mx8-nxp-bsp = "1.20.3.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-good-qt:mx8-nxp-bsp = "1.20.3.imx"
PREFERRED_VERSION_gstreamer1.0-plugins-ugly:mx8-nxp-bsp = "1.20.%"
PREFERRED_VERSION_gstreamer1.0:mx8-nxp-bsp              = "1.20.3.imx"
PREFERRED_VERSION_gstreamer1.0-libav:mx8-nxp-bsp        = "1.20.%"
PREFERRED_VERSION_gstreamer1.0-rtsp-server:mx8-nxp-bsp  = "1.20.%"

PREFERRED_VERSION_opengl-es-cts:aarch64 = "3.2.8.0"

# settings taken from fsl-imx-base.inc
DEFAULTTUNE:mx8-nxp-bsp    ?= "armv8a-crc-crypto"
DEFAULTTUNE:mx8m-nxp-bsp   ?= "armv8a-crc-crypto"
DEFAULTTUNE:mx9-nxp-bsp    ?= "armv8a-crc-crypto"

PACKAGECONFIG:append:pn-gstreamer1.0-plugins-ugly = " x264"
PACKAGECONFIG:append:pn-systemd = " unmanaged-network"
