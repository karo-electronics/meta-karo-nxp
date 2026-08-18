SUMMARY = "A very basic Wayland image with Weston desktop and terminal"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

require recipes-graphics/images/core-image-weston.bb
require karo-image.inc

IMAGE_FEATURES:remove = "ssh-server-dropbear"
IMAGE_FEATURES:append = " ssh-server-openssh"

CORE_IMAGE_BASE_INSTALL:append = " \
    glmark2 \
    weston \
    weston-init \
    weston-examples \
"

IMAGE_INSTALL:append = " \
    gst-examples \
    libdrm \
    libdrm-tests \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'weston-xwayland xterm', '', d)} \
    packagegroup-imx-isp \
    packagegroup-imx-security \
    packagegroup-fsl-gstreamer1.0 \
    packagegroup-fsl-gstreamer1.0-full \
"

export IMAGE_BASENAME = "karo-image-weston"
