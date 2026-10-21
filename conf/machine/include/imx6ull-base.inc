MACHINEOVERRIDES =. "mx6ull:"

require imx6ul-base.inc

SOC_FAMILY = "imx6ull"
# The layer ships imx6ul_defconfig only; ULL reuses it.
KBUILD_DEFCONFIG = "imx6ul_defconfig"
