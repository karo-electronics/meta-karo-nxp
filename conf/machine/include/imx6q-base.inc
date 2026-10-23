MACHINEOVERRIDES =. "mx6q:"

require imx6-base.inc
require conf/machine/include/arm/armv7a/tune-cortexa9.inc

SOC_PREFIX = "imx6q"
SOC_FAMILY = "imx6q"

# No Vivante stack: drop the gpu tags.
MACHINEOVERRIDES_EXTENDER:mx6q:use-nxp-bsp = "imx-generic-bsp:imx-nxp-bsp:imxfbdev:imxipu:imxvpu:mx6-generic-bsp:mx6-nxp-bsp:mx6q-generic-bsp:mx6q-nxp-bsp"
# No GPU: mainline weston, not the fbdev fork.
PREFERRED_VERSION_weston:mx6q-nxp-bsp = "13.0.1"
