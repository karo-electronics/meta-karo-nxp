# Copyright (C) 2013-2020 Lothar Waßmann <LW@KARO-electronics.de>
# based on: meta-imx/meta-bsp/recipes-bsp/u-boot/u-boot-imx_2020.04.bb
#   Copyright (C) 2013-2016 Freescale Semiconductor
#   Copyright 2017-2020 NXP

DESCRIPTION = "i.MX U-Boot suppporting Ka-Ro electronics boards."
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native dtc-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"
PE = "1"

S = "${WORKDIR}/git"

require recipes-bsp/u-boot/u-boot.inc
inherit fsl-u-boot-localversion

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

UBOOT_SRC ?= "git://github.com/karo-electronics/karo-tx-uboot.git;protocol=https"
SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH}"
SRCBRANCH = "karo-tx8m"

# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "fa28ee98d75792f9150b48303894b6a6c429057a"

S = "${WORKDIR}/git"

LOCALVERSION ?= "-5.4.24-2.1.0"

BOOT_TOOLS_mx8m = "imx-boot-tools"

do_deploy_append_mx8m () {
    install -d "${DEPLOYDIR}/${BOOT_TOOLS}"

    # Deploy u-boot-nodtb.bin and dtb file, to be packaged in boot binary by imx-boot
    if [ -n "${UBOOT_CONFIG}" ];then
        for config in ${UBOOT_MACHINE};do
            i=$(expr $i + 1)
            for type in ${UBOOT_CONFIG};do
                j=$(expr $j + 1)
                if [ $j -eq $i ];then
                    install -m 0644 "${B}/${config}/arch/arm/dts/${UBOOT_DTB_NAME}" "${DEPLOYDIR}/${BOOT_TOOLS}"
                    install -m 0644 "${B}/${config}/u-boot-nodtb.bin" "${DEPLOYDIR}/${BOOT_TOOLS}/u-boot-nodtb.${UBOOT_SUFFIX}-${MACHINE}-${type}"
                fi
            done
            unset  j
        done
        unset  i
    fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx8)"
