FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}:${THISDIR}/${BP}/patches:${THISDIR}/${BP}/env:"
SRC_URI:append = " \
    file://karo.bmp;subdir=git/tools/logos \
"

SRC_URI:append = " \
    file://dts/imx93-karo.dtsi;subdir=git/arch/arm \
"

SRC_URI:append = " \
    file://dts/${DTB_BASENAME}-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/${DTB_BASENAME}.dts;subdir=git/arch/arm \
"

UBOOT_FEATURES:append = "${@ bb.utils.contains('DISTRO_FEATURES', "copro", " copro", "", d)}"
UBOOT_FEATURES:append = " fastboot"
