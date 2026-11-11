SUMMARY = "Linux Kernel for Ka-Ro electronics Computer-On-Modules"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}/cfg:${THISDIR}/${BP}/defconfigs:"

require recipes-kernel/linux/linux-karo.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KERNEL_SRC_DEFAULT = "git://github.com/karo-electronics/karo-tx-linux.git;protocol=https"
KERNEL_BRANCH_DEFAULT = "lf-6.6.y-karo-2"
KERNEL_REV_DEFAULT = "75b47aa2bad42709d81ed503aba520adab62c80e"

CVE_PRODUCT = "linux:linux_kernel"
CVE_VERSION = "6.6.52"

SPDX_INCLUDE_KERNEL_CONFIG = "1"

# meta-freescale linux-imx.inc:40: uImage load address from imx-base.inc per SoC.
KERNEL_EXTRA_ARGS:append:mx6-nxp-bsp = " LOADADDR=${UBOOT_ENTRYPOINT}"

KBUILD_DEFCONFIG ?= "${SOC_FAMILY}_defconfig"

KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','bluetooth',' bluetooth.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','copro',' copro.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','csi-camera',' csi-camera.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','imx219',' imx219.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','ipv6',' ipv6.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','dsi83',' dsi83.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','tc358867',' tc358867.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','lvds',' lvds.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','raspi-display',' raspi-display.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','systemd',' systemd.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','wifi',' wifi.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','pcie',' pcie.cfg','',d)}"
KERNEL_FEATURES:append = "${@' ${KARO_BOARD_PMIC}.cfg' if d.getVar('KARO_BOARD_PMIC') != '' else ''}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','flexcan',' flexcan.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','nfs-server',' nfs-server.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES',"rauc"," rauc.cfg","",d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES',"edac"," edac.cfg","",d)}"

KERNEL_FEATURES:append:mx93-nxp-bsp = " lvds.cfg"

COMPATIBLE_MACHINE = "(mx6-nxp-bsp|mx[89]-nxp-bsp)"
