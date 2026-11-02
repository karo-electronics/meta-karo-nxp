FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:${THISDIR}/${PN}/patches:${THISDIR}/${PN}/defconfigs:${THISDIR}/${PN}/cfg:"

inherit fsl-kernel-localversion
LOCALVERSION = "-karo"

SRC_URI += "file://0001-media-i2c-imx219-rework-timing-setup-and-stream-cont.patch"
SRC_URI += "file://0002-Input-edt-ft5x06-add-polling-fallback-and-touch-rele.patch"
SRC_URI += "file://0003-drm-bridge-tc358762-program-panel-timing-registers-f.patch"
SRC_URI += "file://0004-drm-panel-panel-simple-honor-data-mapping-for-panel-.patch"
SRC_URI += "file://0005-arm64-dts-add-the-imx95-qs95-5610-module-dts-and-ove.patch"

SRC_URI:append = " file://${SOC_FAMILY}_defconfig"

SRC_URI:append = "${@ "".join(map(lambda f: " file://" + f, "${KERNEL_FEATURES}".split()))}"

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
