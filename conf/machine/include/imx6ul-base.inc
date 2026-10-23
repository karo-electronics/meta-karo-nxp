MACHINEOVERRIDES =. "mx6ul:"

require imx6-base.inc
require conf/machine/include/arm/armv7a/tune-cortexa7.inc

SOC_PREFIX = "imx6ul"
SOC_FAMILY = "imx6ul"

# No GPU: mainline weston, not the fbdev fork (imx6q-base.inc:12).
PREFERRED_VERSION_weston:mx6ul-nxp-bsp = "13.0.1"
