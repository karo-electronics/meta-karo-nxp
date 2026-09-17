MACHINEOVERRIDES =. "mx8:mx8m:"

require karo-base.inc
require conf/machine/include/arm/armv8a/tune-cortexa53.inc

SOC_PREFIX = "imx8m"

MACHINE_FEATURES:remove = "nxp8987"

MACHINE_HAS_VIVANTE_KERNEL_DRIVER_SUPPORT = "1"

KARO_BOARD_PMIC ?= "bd71847"

UBOOT_SAVED_DRAM_TIMING_BASE ??= "0x180000"

ATF_PLATFORM ?= "${SOC_FAMILY}"

ATF_BL31_SIZE ??= "0x20000"

IMX_BOOT_UART_BASE ??= "0x30860000"

# Set DDR FIRMWARE
DDR_FIRMWARE_NAME ?= " \
        ddr3_imem_1d.bin \
        ddr3_dmem_1d.bin \
"

PREFERRED_VERSION_opengl-es-cts:aarch64 = "3.2.8.0"

require imx8m-overlays.inc
