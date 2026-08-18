FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://patches/0001-dts-add-dts-files-for-qs95-5610-module-and-qsbase95-.patch \
    file://patches/0002-imx219-update-driver.patch \
    file://patches/0003-drm-add-support-for-the-raspberry-7inch-panel.patch \
"

SRC_URI:append = " \
    file://defconfigs/imx95_defconfig \
"

SRC_URI:append = " \
    file://cfg/raspi-display.cfg \
    file://cfg/lvds-panel.cfg \
    file://cfg/cam.cfg \
"

# Set defconfig for qs95 machine
KBUILD_DEFCONFIG:qs95 = ""

# Create symlink to fix path resolution error when using devtool
do_kernel_metadata:prepend() {
    if [ ! -e "${WORKDIR}/sources" ]; then
        ln -sf . "${WORKDIR}/sources"
    fi
}
