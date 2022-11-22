# Copyright (C) 2013-2020 Lothar Waßmann <LW@KARO-electronics.de>
# based on: meta-imx/meta-bsp/recipes-bsp/u-boot/u-boot-imx_2020.04.bb
#   Copyright (C) 2013-2016 Freescale Semiconductor
#   Copyright 2017-2020 NXP

DESCRIPTION = "i.MX U-Boot suppporting Ka-Ro electronics boards."
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native dtc-native"

require recipes-bsp/u-boot/u-boot.inc
inherit fsl-u-boot-localversion

FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}/defconfigs:${THISDIR}/${BP}/cfg:${THISDIR}/${BP}/env:"

PROVIDES += "u-boot"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"
PE = "1"

UBOOT_SRC ?= "git://github.com/karo-electronics/karo-tx-uboot.git;protocol=https"
SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH}"
SRCBRANCH = "lf_v2020.04-karo"
SRCREV = "fc4bde81496059a71c1c161b1da9471e539ca4b7"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

LOCALVERSION ?= "-5.10.9-1.0.0"

SRC_URI:append = "${@ "".join(map(lambda f: " file://%s.cfg" % f, d.getVar('UBOOT_FEATURES').split()))}"

SRC_URI:append:mx8m-nxp-bsp = " file://${MACHINE}_env.txt;subdir=git/board/karo/tx8m"

SRC_URI:append = "${@ " file://dts/${UBOOT_DTB_NAME}".replace(".dtb", ".dts") + ";subdir=git/arch/arm"}"
SRC_URI:append = "${@ " file://dts/${UBOOT_DTB_NAME}".replace(".dtb", "-u-boot.dtsi") + ";subdir=git/arch/arm"}"

SRC_URI:append = " file://${MACHINE}_defconfig.template"
SRC_URI:append = "${@ "".join(map(lambda f: " file://u-boot-cfg.%s" % f, d.getVar('UBOOT_CONFIG').split()))}"

EXTRA_OEMAKE:append = " V=0"

do_configure:prepend() {
    if [ -n "${UBOOT_CONFIG}" ];then
        i=0
        for config in ${UBOOT_MACHINE};do
            i=$(expr $i + 1)
            j=0
            for type in ${UBOOT_CONFIG};do
                j=$(expr $j + 1)
                [ $j -lt $i ] && continue
                c="`echo "$config" | sed 's/_config/_defconfig/'`"
                bbnote "Copying '${MACHINE}_defconfig.template' to '${S}/configs/${c}'"
                cp "${WORKDIR}/${MACHINE}_defconfig.template" "${S}/configs/${c}"
                if [ -s "${WORKDIR}/u-boot-cfg.${type}" ];then
                    bbnote "Appending '$type' specific config to '${S}/configs/${c}'"
                    cat "${WORKDIR}/u-boot-cfg.${type}" >> "${S}/configs/${c}"
                fi
                break
            done
        done
        unset i j
    else
        cp "${WORKDIR}/${MACHINE}_defconfig.template" "${S}/configs/${MACHINE}_defconfig"
    fi
}

do_configure:append() {
    tmpfile="`mktemp cfg-XXXXXX.tmp`"
    if [ "${KARO_BASEBOARD}" != "" ];then
        if [ -z "$tmpfile" ];then
            bbfatal "Failed to create tmpfile"
        fi
        cat <<EOF >> "$tmpfile"
CONFIG_DEFAULT_DEVICE_TREE="${DTB_BASENAME}-${KARO_BASEBOARD}"
EOF
        grep -q "${DTB_BASENAME}-${KARO_BASEBOARD}\.dtb" ${S}/arch/arm/dts/Makefile || \
                sed -i '/^targets /i\
dtb-y += ${DTB_BASENAME}-${KARO_BASEBOARD}.dtb\
' ${S}/arch/arm/dts/Makefile
    fi
    if [ "${UBOOT_ENV_FILE}" != "${MACHINE}_env.txt" ];then
        cat <<EOF >> "$tmpfile"
CONFIG_DEFAULT_ENV_FILE="board/\$(VENDOR)/\$(BOARD)/${UBOOT_ENV_FILE}"
EOF
    fi
    if [ -n "${UBOOT_CONFIG}" ];then
        for config in ${UBOOT_MACHINE};do
            c="${B}/${config}"
            merge_config.sh -m -r -O "${c}" "${c}/.config" "$tmpfile"
            oe_runmake -C ${c} oldconfig
        done
    else
        merge_config.sh -m -r -O "${B}" "${B}/.config" "$tmpfile"
        oe_runmake -C "${B}" oldconfig
    fi
    rm -f "$tmpfile"
}

do_deploy:append:mx8m-nxp-bsp () {
    install -d "${DEPLOYDIR}/${BOOT_TOOLS}"

    # Deploy u-boot-nodtb.bin and dtb file, to be packaged in boot binary by imx-boot
    if [ -n "${UBOOT_CONFIG}" ];then
        i=0
        for config in ${UBOOT_MACHINE};do
            i=$(expr $i + 1)
            j=0
            for type in ${UBOOT_CONFIG};do
                j=$(expr $j + 1)
                if [ $j -eq $i ];then
                    install -m 0644 "${B}/${config}/arch/arm/dts/${UBOOT_DTB_NAME}" "${DEPLOYDIR}/${BOOT_TOOLS}"
                    install -m 0644 "${B}/${config}/u-boot-nodtb.bin" "${DEPLOYDIR}/${BOOT_TOOLS}/u-boot-nodtb.${UBOOT_SUFFIX}-${MACHINE}-${type}"
                fi
            done
            unset j
        done
        unset i
    else
        install -m 0644 "${B}/arch/arm/dts/${UBOOT_DTB_NAME}" "${DEPLOYDIR}/${BOOT_TOOLS}"
        install -m 0644 "${B}/u-boot-nodtb.bin" "${DEPLOYDIR}/${BOOT_TOOLS}/u-boot-nodtb.${UBOOT_SUFFIX}-${MACHINE}-${type}"
    fi
}

do_savedefconfig() {
    if [ -n "${UBOOT_CONFIG}" ];then
        for config in ${UBOOT_MACHINE};do
            bbplain "Saving defconfig to:\n${B}/${config}/defconfig"
            oe_runmake -C ${B}/${config} oldconfig
            oe_runmake -C ${B}/${config} savedefconfig
        done
    else
        bbplain "Saving defconfig to:\n${B}/defconfig"
        oe_runmake -C ${B} oldconfig
        oe_runmake -C ${B} savedefconfig
    fi
}
do_savedefconfig[nostamp] = "1"
addtask savedefconfig after do_configure

addtask do_configure before do_devshell

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx8)"
