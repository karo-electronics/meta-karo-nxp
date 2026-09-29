MACHINEOVERRIDES =. "mx6ul:"

require imx6-base.inc
require conf/machine/include/arm/armv7a/tune-cortexa7.inc

SOC_PREFIX = "imx6ul"
SOC_FAMILY = "imx6ul"

# Overlay defaults; the MB7 carrier is the karo-mb7 overlay.
DTB_OVERLAY_INCLUDES ??= ""
