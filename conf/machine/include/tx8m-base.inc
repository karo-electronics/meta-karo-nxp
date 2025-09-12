require karo-base.inc
require conf/machine/include/arm/armv8a/tune-cortexa53.inc

MACHINEOVERRIDES =. "mx8:mx8m:"

require ${@ "imx8mm-base.inc" if 'mx8mm' in d.getVar('MACHINEOVERRIDES').split(':') else ""}
require ${@ "imx8mn-base.inc" if 'mx8mn' in d.getVar('MACHINEOVERRIDES').split(':') else ""}
require ${@ "imx8mp-base.inc" if 'mx8mp' in d.getVar('MACHINEOVERRIDES').split(':') else ""}

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
