MACHINEOVERRIDES =. "mx6dl:"

require imx6-base.inc
require conf/machine/include/arm/armv7a/tune-cortexa9.inc

SOC_PREFIX = "imx6dl"
SOC_FAMILY = "imx6dl"

# No Vivante stack: drop the gpu tags.
MACHINEOVERRIDES_EXTENDER:mx6dl:use-nxp-bsp = "imx-generic-bsp:imx-nxp-bsp:imxfbdev:imxpxp:imxipu:imxvpu:imxepdc:mx6-generic-bsp:mx6-nxp-bsp:mx6dl-generic-bsp:mx6dl-nxp-bsp"
# No GPU: mainline weston, not the fbdev fork.
PREFERRED_VERSION_weston:mx6dl-nxp-bsp = "13.0.1"

# Overlay defaults; i.MX6 baseboards use full device trees.
DTB_OVERLAY_INCLUDES ??= ""
DTB_OVERLAYS ??= ""
DTB_OVERLAYS_generic ??= ""
