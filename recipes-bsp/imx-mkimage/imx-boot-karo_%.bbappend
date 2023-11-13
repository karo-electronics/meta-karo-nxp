FILESEXTRAPATHS:prepend := "${THISDIR}/mkimage:"
SRC_URI:append = " \
    file://make-clean-bugfix.patch \
"

SRC_URI:append:mx9-nxp-bsp = " \
    file://use-u-boot-mkimage.patch \
"

do_compile:prepend() {
    export dtbs=${UBOOT_DTB_NAME}
}
