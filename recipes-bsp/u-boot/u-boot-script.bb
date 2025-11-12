LICENSE = "MIT"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

DEPENDS = "u-boot-mkimage-native"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI = "file://uboot.sh"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

inherit deploy

UBOOT_INSTALL_BOOT_DIR ?= "/boot"

do_compile () {
    mkimage -A arm64 -O linux -T script -C none -a 0 -e 0 \
                  -n "boot script" -d ${WORKDIR}/uboot.sh ${B}/boot.scr.uimg
}

do_install () {
    install -d ${D}/${UBOOT_INSTALL_BOOT_DIR}
    install -D -m 755 ${B}/boot.scr.uimg ${D}/${UBOOT_INSTALL_BOOT_DIR}/boot.scr.uimg
    install -D -m 755 ${WORKDIR}/uboot.sh ${D}/${UBOOT_INSTALL_BOOT_DIR}/uboot.sh
}

FILES:${PN} += "${UBOOT_INSTALL_BOOT_DIR}"
