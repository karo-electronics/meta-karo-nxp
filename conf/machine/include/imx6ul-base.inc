MACHINEOVERRIDES =. "mx6ul:"

require imx6-base.inc
require conf/machine/include/arm/armv7a/tune-cortexa7.inc

SOC_PREFIX = "imx6ul"
SOC_FAMILY = "imx6ul"

# Overlay defaults; i.MX6 baseboards use full device trees.
DTB_OVERLAY_INCLUDES ??= ""
