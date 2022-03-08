# Ka-Ro specific kernel source for NXP's linux-imx 5.10
KERNEL_SRC = "git://github.com/karo-electronics/karo-tx-linux.git;protocol=https"
SRCBRANCH = "imx_5.10.9_1.0.0"
SRCREV = "7f1c87a753d89b53f71ce176c25bf08d37c9aae4"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.10/patches:${THISDIR}/${PN}-5.10:"
SRC_URI_append = " \
	${@' file://cfg/'.join("${KERNEL_FEATURES}".split(" "))} \
	file://0001-lib-iov_iter-initialize-flags-in-new-pipe_buffer.patch \
"
SRC_URI_remove = "file://defconfig"

SRC_URI_append_mx8 = " \
	file://dts/freescale/imx8m-qs8m-dsi83.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8m-qs8m-raspi-display.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8m-qs8m-tc358867.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-cam.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-dsi83-cam.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-dsi83.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-raspi-display-cam.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-raspi-display.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-tc358867.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-mq00.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qs8m-raspi-camera.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase3-dsi83.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase3-laird.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase3-raspi-display.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase3-tc358867.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase3.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-qsxm-mm60.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1610-mipi-mb.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1610.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1620-lvds-mb.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1620-mb7.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-1620.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-lvds-mb.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-mipi-mb.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m-mb7.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mm-tx8m.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase2-dsi83.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase2-raspi-display.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase2-tc358867.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase2.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-qs8m-nd00.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-tx8m-mipi-mb.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-tx8m-nd00-mipi-mb.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mn-tx8m-nd00.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-karo.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-dsi83.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-laird.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-laird.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-raspi-display.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-raspi-display.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-tc358867.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-qsxp-ml81.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-tx8p-lvds-mb.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-tx8p-mb7.dtsi;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-tx8p-ml81-lvds-mb.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-tx8p-ml81-mb7.dts;subdir=git/arch/arm64/boot \
	file://dts/freescale/imx8mp-tx8p-ml81.dts;subdir=git/arch/arm64/boot \
"

KARO_BOARD_PMIC ??= ""

KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES','bluetooth',' bluetooth.cfg','',d)}"
KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES','csi-camera',' csi-camera.cfg','',d)}"
KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES','imx219',' imx219.cfg','',d)}"
KERNEL_FEATURES_append_mx8 = "${@bb.utils.contains('DISTRO_FEATURES','imx219',' mx8-cam.cfg','',d)}"
KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES','ipv6',' ipv6.cfg','',d)}"
KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES','dsi83',' dsi83.cfg','',d)}"
KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES','tc358867',' tc358867.cfg','',d)}"
KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES','lvds',' lvds.cfg','',d)}"
KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES','raspi-display',' raspi-display.cfg','',d)}"
KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES','systemd',' systemd.cfg','',d)}"
KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES','wifi',' wifi.cfg','',d)}"
KERNEL_FEATURES_append = "${@bb.utils.contains('DISTRO_FEATURES','pcie',' pcie.cfg apex.cfg','',d)}"
KERNEL_FEATURES_append = "${@' ${KARO_BOARD_PMIC}.cfg' if d.getVar('KARO_BOARD_PMIC') != '' else ''}"

SRC_URI_append_mx8mm = " \
	file://mx8mm_defconfig;subdir=git/arch/arm64/configs \
"
SRC_URI_append_mx8mn = " \
	file://mx8mn_defconfig;subdir=git/arch/arm64/configs \
"
SRC_URI_append_mx8mp = " \
	file://mx8mp_defconfig;subdir=git/arch/arm64/configs \
"

SRC_URI_append_qs8m = " \
	file://0001-mx6s-capture-add-rggb8-video-format.patch \
"

# remove any defconfig added via SRC_URI
SRC_URI_remove = "file://defconfig"

KBUILD_DEFCONFIG_mx8mm = "mx8mm_defconfig"
KBUILD_DEFCONFIG_mx8mn = "mx8mn_defconfig"
KBUILD_DEFCONFIG_mx8mp = "mx8mp_defconfig"

DEFCONFIG_PATH_mx8 = "arch/${ARCH}/configs"

# with these two tasks overwritten files in kernel-source are reset,
# so we delete them to keep our changes made by yocto
deltask kernel_checkout
deltask validate_branches

do_preconfigure_prepend () {
    if [ -z "${KBUILD_DEFCONFIG}" ];then
        bbfatal "KBUILD_DEFCONFIG is not set"
    fi
    install -v ${S}/${DEFCONFIG_PATH}/${KBUILD_DEFCONFIG} ${WORKDIR}/defconfig
    for cfg in ${KERNEL_FEATURES};do
        bbnote "Merging ${cfg} into ${WORKDIR}/defconfig"
        sed -i "$(sed -n '/CONFIG_/{s:^\(# \)\?:/:;s:[= ].*$:/d:;p}' ${WORKDIR}/cfg/${cfg})" ${WORKDIR}/defconfig
        cat ${WORKDIR}/cfg/${cfg} >> ${WORKDIR}/defconfig
    done
}
