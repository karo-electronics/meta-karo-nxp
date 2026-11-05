MACHINEOVERRIDES =. "mx95:"

require karo-base.inc
require conf/machine/include/arm/armv8-2a/tune-cortexa55.inc

SOC_PREFIX = "imx95"

MACHINE_FEATURES:append = " quickboot pci optee gpu"

ATF_PLATFORM = "imx95"
OEI_CORE   = "m33"
OEI_SOC    = "mx95"
OEI_BOARD  ?= "mx95lp4x-15"
DDR_TYPE   ?= "lpddr4"

SYSTEM_MANAGER_CONFIG = "krimx95"
SYSTEM_MANAGER_MONITOR_MODE ?= "0"

IMXBOOT_TARGETS = "${IMXBOOT_TARGETS_BASENAME}_a55"

IMX_BOOT_SOC_TARGET = "iMX95"
IMX_BOOT_SEEK = "32"

require imx95-overlays.inc
