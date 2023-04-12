SUMMARY = "Linux Kernel for Ka-Ro electronics Computer-On-Modules"

require recipes-kernel/linux/linux-karo.inc

DEPENDS += "lzop-native bc-native"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRCBRANCH = "lf-5.15.y-karo"
SRCREV = "1bc4508ac322388f231561cd06b6d49281ac1b1c"
KERNEL_SRC = "git://github.com/karo-electronics/karo-tx-linux.git"
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-5.15/patches:${THISDIR}/${PN}-5.15:"

PROVIDES += "linux"

SRC_URI = "${KERNEL_SRC};protocol=https;branch=${SRCBRANCH}"

SRC_URI:append = "${@ "".join(map(lambda f: " file://cfg/" + f, "${KERNEL_FEATURES}".split()))}"

# automatically add all .dts files referenced by ${KERNEL_DEVICETREE} to SRC_URI
SRC_URI:append:mx8-nxp-bsp = "${@"".join(map(lambda f: " file://dts/%s;subdir=git/${KERNEL_OUTPUT_DIR}" % f.replace(".dtb", ".dts"), "${KERNEL_DEVICETREE}".split()))}"

SRC_URI:append:mx8-nxp-bsp = " \
	file://dts/freescale/imx8m-qs8m-dsi83.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8m-qs8m-raspi-display.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8m-qs8m-tc358867.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8m-qs8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8m-tx8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-cam.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-dsi83-cam.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-dsi83.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-raspi-display-cam.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-raspi-display.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2-tc358867.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase2.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qs8m-mq00.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qs8m-mq00-qsbase4.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qs8m-raspi-camera.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase3-dsi83.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase3-laird.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase3-raspi-display.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase3-tc358867.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase3.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase4-raspi-display.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qsxm-mm60-qsbase4.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-qsxm-mm60.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-1610-mipi-mb-wifi.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-1610-mipi-mb.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-1610.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-1620-lvds-mb-wifi.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-1620-lvds-mb.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-1620-mb7.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-1620.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-1622.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-ath9k-wifi.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-lvds-mb.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-mipi-mb.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m-mb7.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase2-dsi83.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase2-raspi-display.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase2-tc358867.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase2.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mn-qs8m-nd00-qsbase4.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mn-qs8m-nd00.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mn-tx8m-mipi-mb.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mn-tx8m-nd00-mipi-mb.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mn-tx8m-nd00.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-karo.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-dsi83.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase4-dsi83.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-laird.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-laird.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-raspi-display.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-raspi-display.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3-tc358867.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase4.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase3.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81-qsbase4.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-qsxp-ml81.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-tx8p-lvds-mb.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-tx8p-mb7.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-tx8p-ml81-lvds-mb.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-tx8p-ml81-mb7.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-tx8p-ml81.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-tx8p-ml82-lvds-mb.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-tx8p-ml82-mb7.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-tx8p-ml82.dts;subdir=git/${KERNEL_OUTPUT_DIR} \
"

SRC_URI:append:mx8-nxp-bsp = " \
	file://0003-vf610-gpio.patch \
"

KARO_BOARD_PMIC ??= ""

SRC_URI:append:mx8mm-nxp-bsp = " \
	file://mx8mm_defconfig \
"
SRC_URI:append:mx8mn-nxp-bsp = " \
	file://mx8mn_defconfig \
"
SRC_URI:append:mx8mp-nxp-bsp = " \
	file://mx8mp_defconfig \
"

SRC_URI:append:qs8m = " \
	file://0001-mx6s-capture-add-rggb8-video-format.patch \
	file://0002-imx219-driver-zeus-version.patch \
"

KERNEL_LOCALVERSION = "${LINUX_VERSION_EXTENSION}"
KERNEL_IMAGETYPE = "Image"

KBUILD_DEFCONFIG:mx8mm-nxp-bsp = "mx8mm_defconfig"
KBUILD_DEFCONFIG:mx8mn-nxp-bsp = "mx8mn_defconfig"
KBUILD_DEFCONFIG:mx8mp-nxp-bsp = "mx8mp_defconfig"

KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','basler',' basler.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','bluetooth',' bluetooth.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','csi-camera',' csi-camera.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','imx219',' imx219.cfg mx8-cam.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','ipv6',' ipv6.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','dsi83',' dsi83.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','tc358867',' tc358867.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','lvds',' lvds.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','raspi-display',' raspi-display.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','systemd',' systemd.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','wifi',' wifi.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','pcie',' pcie.cfg apex.cfg','',d)}"
KERNEL_FEATURES:append = "${@' ${KARO_BOARD_PMIC}.cfg' if d.getVar('KARO_BOARD_PMIC') != '' else ''}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','flexcan',' flexcan.cfg','',d)}"

KERNEL_FEATURES:append:tx8m-1620 = " no-suspend.cfg"
KERNEL_FEATURES:append:tx8m-1622 = " no-suspend.cfg"

COMPATIBLE_MACHINE = "(mx8-nxp-bsp)"

EXTRA_OEMAKE:append = " V=0"
KERNEL_DTC_FLAGS += "-@"

do_configure:prepend() {
    # Add GIT revision to the local version
    head=`git --git-dir=${S}/.git rev-parse --verify --short HEAD 2> /dev/null`
    if ! [ -s "${S}/.scmversion" ] || ! grep -q "$head" ${S}/.scmversion;then
        echo "+g$head" > "${S}/.scmversion"
    fi
    install -v "${WORKDIR}/${KBUILD_DEFCONFIG}" "${B}/.config"
    sed -i '/CONFIG_LOCALVERSION/d' "${B}/.config"
    echo 'CONFIG_LOCALVERSION="${KERNEL_LOCALVERSION}"' >> "${B}/.config"

    for f in ${KERNEL_FEATURES};do
        cat ${WORKDIR}/cfg/$f >> ${B}/.config
    done
}
addtask do_configure before do_devshell

do_compile_dtbs() {
    oe_runmake -C ${B} DTC_FLAGS="${KERNEL_DTC_FLAGS}" ${KERNEL_DEVICETREE}
}
addtask do_compile_dtbs before do_compile after do_configure
