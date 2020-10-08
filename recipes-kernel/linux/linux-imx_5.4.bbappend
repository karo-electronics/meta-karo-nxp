# Ka-Ro specific patch set for NXP's linux-imx 5.4.24
PROVIDES += "linux"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}/patches:${THISDIR}/${PN}-${PV}:"
SRC_URI_append = " \
	file://0001-TI-SN65DSI83-bridge-driver-support.patch \
	file://0002-Little-fixes-for-imx-drm-drivers.patch \
	file://0003-Add-RaspberryPi-7inch-touchscreen-display-support-dr.patch \
	file://0004-Revert-MLK-23131-2-soc-imx-busfreq-imx8mq-Correct-dr.patch \
	file://0005-pca9450-bugfix.patch \
	${@bb.utils.contains('DISTRO_FEATURES','systemd','file://systemd.cfg','',d)} \
	${@bb.utils.contains('DISTRO_FEATURES','wifi','file://wifi.cfg','',d)} \
"

SRC_URI_append_mx8 = " \
	file://dts/freescale/imx8m-qs8m-dsi83.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8m-qs8m-raspi-display.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-dsi83.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-raspi-display.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase3.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qsxm-mm60.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1610-mipi-mb.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1610-tx4etml0500.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1610.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1620-lvds-mb.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1620.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-lvds-mb.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-mipi-mb.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase2-dsi83.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase2-raspi-display.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase2.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-qs8m-nd00.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-tx8m-mipi-mb.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-tx8m-nd00-mipi-mb.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-tx8m-nd00.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-qsxp-ml81.dts;subdir=git/arch/arm64/boot \
"
python() {
    if d.getVar('KARO_BOARD_PMIC'):
        d.appendVar('SRC_URI', " file://${KARO_BOARD_PMIC}.cfg")
}

SRC_URI_append_mx8mm = " \
	file://mx8mm_defconfig;subdir=git/arch/arm64/configs \
"
SRC_URI_append_mx8mn = " \
	file://mx8mn_defconfig;subdir=git/arch/arm64/configs \
"
SRC_URI_append_mx8mp = " \
	file://mx8mp_defconfig;subdir=git/arch/arm64/configs \
"

KBUILD_DEFCONFIG_mx8mm = "mx8mm_defconfig"
KBUILD_DEFCONFIG_mx8mn = "mx8mn_defconfig"
KBUILD_DEFCONFIG_mx8mp = "mx8mp_defconfig"

DEFCONFIG_PATH_mx8 = "arch/${ARCH}/configs"

do_preconfigure_prepend () {
    if [ -z "${KBUILD_DEFCONFIG}" ];then
        bbfatal "KBUILD_DEFCONFIG is not set"
    fi
    install -v ${S}/${DEFCONFIG_PATH}/${KBUILD_DEFCONFIG} ${WORKDIR}/defconfig
    for cfg in $(ls -d ${WORKDIR}/* | grep "\\.cfg$");do
        bbnote "Merging ${cfg} into ${WORKDIR}/defconfig"
        cat ${cfg} >> ${WORKDIR}/defconfig
    done
}
