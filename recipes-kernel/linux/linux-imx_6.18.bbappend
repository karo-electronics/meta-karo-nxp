FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:${THISDIR}/${PN}/patches:${THISDIR}/${PN}/defconfigs:${THISDIR}/${PN}/cfg:"

inherit fsl-kernel-localversion
LOCALVERSION = "-karo"

SRC_URI:append = " \
    file://0001-dts-add-dts-files-for-qs95-5610-module-and-qsbase95-.patch \
    file://0002-imx219-update-driver.patch \
    file://0003-drm-add-support-for-the-raspberry-7inch-panel.patch \
    file://0004-dts-split-qsbase95-baseboard-dts-into-per-device-ove.patch \
"

SRC_URI:append = " file://${SOC_FAMILY}_defconfig"

SRC_URI:append = "${@ "".join(map(lambda f: " file://" + f, "${KERNEL_FEATURES}".split()))}"

KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','csi-camera',' csi-camera.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','imx219',' imx219.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','ipv6',' ipv6.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','dsi83',' dsi83.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','lvds',' lvds.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','raspi-display',' raspi-display.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','pcie',' pcie.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','flexcan',' flexcan.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','nfs-server',' nfs-server.cfg','',d)}"

# Set KBUILD_DEFCONFIG to empty so that the defconfig specified in SRC_URI is automatically used
KBUILD_DEFCONFIG:mx9-generic-bsp = ""

# Create symlink to fix path resolution error when using devtool
do_kernel_metadata:prepend() {
    if [ ! -e "${WORKDIR}/sources" ]; then
        ln -sf . "${WORKDIR}/sources"
    fi
}
