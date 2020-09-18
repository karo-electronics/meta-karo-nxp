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
	file://dts/freescale/*;subdir=git/arch/arm64/boot \
"
SRC_URI_append_mx8mm = " \
	file://tx8mm_defconfig;subdir=git/arch/arm64/configs \
"
SRC_URI_append_mx8mn = " \
	file://tx8mn_defconfig;subdir=git/arch/arm64/configs \
"
SRC_URI_append_mx8mp = " \
	file://tx8mp_defconfig;subdir=git/arch/arm64/configs \
"

KBUILD_DEFCONFIG_mx8mm = "tx8mm_defconfig"
KBUILD_DEFCONFIG_mx8mn = "tx8mn_defconfig"
KBUILD_DEFCONFIG_mx8mp = "tx8mp_defconfig"

DEFCONFIG_PATH_mx8 = "arch/${ARCH}/configs"

do_preconfigure_prepend () {
    if [ -z "${KBUILD_DEFCONFIG}" ];then
        bbfatal "KBUILD_DEFCONFIG is not set"
    fi
    install -v ${S}/${DEFCONFIG_PATH}/${KBUILD_DEFCONFIG} ${WORKDIR}/defconfig
    shopt -s nullglob
    for cfg in ${WORKDIR}/*.cfg;do
        bbnote "Merging ${cfg} into ${WORKDIR}/defconfig"
        cat ${cfg} >> ${WORKDIR}/defconfig
    done
}
