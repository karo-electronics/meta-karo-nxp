require karo-base.inc
require conf/machine/include/arm/armv8-2a/tune-cortexa55.inc

MACHINEOVERRIDES =. "mx91:"

SOC_PREFIX = "imx91"

KARO_BOARD_PMIC ?= "pca9450"

ATF_PLATFORM ?= "imx91"

ATF_BL31_BASE ??= "0x204c0000"
ATF_BL31_SIZE ??= "0x20000"

UBOOT_SAVED_DRAM_TIMING_BASE ??= "0x20490000"

IMX_BOOT_UART_BASE ??= "0x44380000"

# Set DDR FIRMWARE
DDR_FIRMWARE_NAME ?= " \
        lpddr4_imem_1d_v202201.bin \
        lpddr4_dmem_1d_v202201.bin \
        lpddr4_imem_2d_v202201.bin \
        lpddr4_dmem_2d_v202201.bin \
"

PREFERRED_VERSION_opengl-es-cts:aarch64 = "3.2.7.0"
