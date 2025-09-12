require karo-base.inc
require conf/machine/include/arm/armv8-2a/tune-cortexa55.inc

MACHINEOVERRIDES =. "mx93:"

SOC_PREFIX = "imx93"

KARO_BOARD_PMIC ?= "pca9450"

#SECO_FIRMWARE_NAME = "mx93a1-ahab-container.img"

ATF_PLATFORM ?= "imx93"

ATF_BL31_BASE ??= "0x204e0000"
ATF_BL31_SIZE ??= "0x25000"

UBOOT_SAVED_DRAM_TIMING_BASE ??= "0x2051c000"

IMX_BOOT_UART_BASE ??= "0x44380000"

# Set DDR FIRMWARE
DDR_FIRMWARE_NAME ?= " \
        lpddr4_imem_1d_v202201.bin \
        lpddr4_dmem_1d_v202201.bin \
        lpddr4_imem_2d_v202201.bin \
        lpddr4_dmem_2d_v202201.bin \
"

PREFERRED_VERSION_opengl-es-cts:aarch64 = "3.2.7.0"
