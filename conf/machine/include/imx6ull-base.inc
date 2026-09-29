MACHINEOVERRIDES =. "mx6ull:"

require imx6-base.inc
require conf/machine/include/arm/armv7a/tune-cortexa7.inc

SOC_PREFIX = "imx6ul"
SOC_FAMILY = "imx6ull"
# The layer ships imx6ul_defconfig only; ULL reuses it.
KBUILD_DEFCONFIG = "imx6ul_defconfig"

# Overlay defaults; the MB7 carrier is the karo-mb7 overlay.
DTB_OVERLAY_INCLUDES ??= ""
