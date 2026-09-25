# i.MX6QuadPlus: the i.MX6Q base with its own SoC family.

require imx6q-base.inc

SOC_FAMILY = "imx6qp"
# The layer ships imx6q_defconfig only; QP reuses it.
KBUILD_DEFCONFIG = "imx6q_defconfig"
DTB_BASENAME = "imx6qp-${MACHINE}"
