MACHINEOVERRIDES =. "mx6q:"

require imx6-base.inc
require conf/machine/include/arm/armv7a/tune-cortexa9.inc

SOC_PREFIX = "imx6q"
SOC_FAMILY = "imx6q"

# Overlay defaults; i.MX6 baseboards use full device trees.
DTB_OVERLAY_INCLUDES ??= ""
DTB_OVERLAYS ??= ""
DTB_OVERLAYS_generic ??= ""
