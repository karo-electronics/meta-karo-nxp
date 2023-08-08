# Copyright (C) 2023 Lothar Waßmann <LW@KARO-electronics.de>
# based on: meta-imx/meta-bsp/recipes-bsp/u-boot/u-boot-imx_2022.04.bb
# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2018 (C) O.S. Systems Software LTDA.
# Copyright 2017-2022 NXP

DESCRIPTION = "i.MX U-Boot suppporting Ka-Ro electronics boards."
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"

DEPENDS += "\
        bc-native \
        bison-native \
        dtc-native \
        flex-native \
        gnutls-native \
        xxd-native \
        python3-setuptools-native \
"

require recipes-bsp/u-boot/u-boot.inc
require conf/machine/include/${SOC_PREFIX}-overlays.inc
inherit fsl-u-boot-localversion

FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}/defconfigs:${THISDIR}/${BP}/cfg:${THISDIR}/${BP}/env:"

PROVIDES += "u-boot"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

UBOOT_SRC ?= "git://github.com/karo-electronics/karo-tx-uboot.git;protocol=https"
SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH}"
SRCBRANCH = "lf_v2022.04-karo"
SRCREV = "4acff84453027573a7dd5b3643a261325855c901"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

LOCALVERSION = "-${SRCBRANCH}-karo"

IMX_EXTRA_FIRMWARE:mx9-nxp-bsp = "firmware-imx-9 firmware-sentinel"

UBOOT_BOARD_DIR = "board/karo/imx93"
UBOOT_ENV_FILE ?= "${@ "%s%s" % (d.getVar('MACHINE'), \
                       "-" + d.getVar('KARO_BASEBOARD') \
                       if d.getVar('KARO_BASEBOARD') != "" else "")}"

SRC_URI:append = "${@ "".join(map(lambda f: " file://%s.cfg" % f, d.getVar('UBOOT_FEATURES').split()))}"

SRC_URI:append = "${@ " file://%s.env;subdir=git/%s" % \
                      (d.getVar('UBOOT_ENV_FILE'), d.getVar('UBOOT_BOARD_DIR')) \
                      if d.getVar('UBOOT_ENV_FILE') != None else ""} \
"

SRC_URI:append = " ${@ " file://dts/%s.dts;subdir=git/arch/arm" % \
                       d.getVar('UBOOT_DTB_NAME').replace(".dtb", "")}"
SRC_URI:append = " ${@ " file://dts/%s-u-boot.dtsi;subdir=git/arch/arm" % \
                       d.getVar('UBOOT_DTB_NAME').replace(".dtb", "")}"

SRC_URI:append = " file://u-boot-cfg.${SOC_PREFIX}"
SRC_URI:append = " file://u-boot-cfg.${SOC_FAMILY}"
SRC_URI:append = " file://u-boot-cfg.${MACHINE}"
SRC_URI:append = "${@ "".join(map(lambda f: " file://u-boot-cfg.%s" % f, d.getVar('UBOOT_CONFIG').split()))}"

EXTRA_OEMAKE:append = " V=0"

do_fetch[prefuncs] =+ "karo_check_baseboard"

python karo_check_baseboard () {
    if d.getVar('KARO_BASEBOARDS') == None:
        bb.fatal("'KARO_BASEBOARDS' is undefined")
    if d.getVar('KARO_BASEBOARD') != "":
        if d.getVar('KARO_BASEBOARD') not in d.getVar('KARO_BASEBOARDS').split():
            raise_sanity_error("Module %s is not supported on Baseboard '%s'; \
                               available baseboards are:\n%s" % \
                               (d.getVar('MACHINE'), d.getVar('KARO_BASEBOARD'), \
                               "\n".join(d.getVar('KARO_BASEBOARDS').split())), d)
}

do_configure:prepend() {
    bbnote "SRC_URI='${SRC_URI}'"
    if [ -n "${UBOOT_CONFIG}" ];then
        i=0
        for config in ${UBOOT_MACHINE};do
            i=$(expr $i + 1)
            j=0
            for type in ${UBOOT_CONFIG};do
                j=$(expr $j + 1)
                [ $j -lt $i ] && continue
                c="`echo "$config" | sed 's/_config/_defconfig/'`"
                bbnote "Copying 'u-boot-cfg.${SOC_PREFIX}' to '${S}/configs/${c}'"
                cp "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${S}/configs/${c}"
                if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
                    bbnote "Appending '${SOC_FAMILY}' specific config to '${S}/configs/${c}'"
                    cat "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" >> "${S}/configs/${c}"
                fi
                bbnote "Appending '${MACHINE}' specific config to '${S}/configs/${c}'"
                cat "${WORKDIR}/u-boot-cfg.${MACHINE}" >> "${S}/configs/${c}"
                if [ -s "${WORKDIR}/u-boot-cfg.${type}" ];then
                    bbnote "Appending '$type' specific config to '${S}/configs/${c}'"
                    cat "${WORKDIR}/u-boot-cfg.${type}" >> "${S}/configs/${c}"
                fi
                break
            done
        done
        unset i j
    else
        bbnote "Copying 'u-boot-cfg.${MACHINE}' to '${S}/configs/${MACHINE}_defconfig'"
        cp "${WORKDIR}/u-boot-cfg.${MACHINE}" "${S}/configs/${MACHINE}_defconfig"
        if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
            bbnote "Appending '${SOC_FAMILY}' specific config to '${S}/configs/${MACHINE}_defconfig'"
            cat "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" >> "${S}/configs/${MACHINE}_defconfig"
        fi
        bbnote "Appending '${MACHINE}' specific config to '${S}/configs/${MACHINE}_defconfig'"
        cat "${WORKDIR}/u-boot-cfg.${MACHINE}" >> "${S}/configs/${MACHINE}_defconfig"
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
    bbnote "UBOOT_ENV_FILE='${UBOOT_ENV_FILE}'"
    if [ -n "${UBOOT_ENV_FILE}" ];then
        cat <<EOF >> "$tmpfile"
CONFIG_DEFAULT_ENV_FILE="board/\$(VENDOR)/\$(BOARD)/${UBOOT_ENV_FILE}.env"
EOF
    else
        echo "# CONFIG_USE_DEFAULT_ENV_FILE is not set" >> "$tmpfile"
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
    rm -vf "$tmpfile"
}

do_compile[depends] += "${@ "".join(map(lambda f: " %s:do_deploy" % f, \
                                        d.getVar('IMX_EXTRA_FIRMWARE').split()))}"

check_cnf() {
    local src="$1"
    local cfg="$2"
    local applied=$(fgrep -f "$src" "${cfg}/.config" | wc -l)
    local configured=$(cat "$src" | wc -l)
    if [ $applied != $configured ];then
        bbwarn "The following items of '$(basename "$src")' have not been accepted by Kconfig"
        bbwarn ">>>$(fgrep -f "$src" "${cfg}/.config" | fgrep -vf - "$src")<<<"
        local p="$(fgrep -f "$src" "${cfg}/.config" | fgrep -vf - "$src" | \
                sed 's/^# //;s/[= ].*$//')"
        local pat
        for pat in $p;do
            if grep -q "$pat" "${cfg}/.config";then
                bbwarn "Actual Kconfig value of '$pat' is: '$(grep "$pat" "${cfg}/.config")'"
            else
                bbwarn "'$pat' is not present in '$(basename "$cfg")/.config"
            fi
        done
        applied="$(fgrep -f "$src" "${cfg}/defconfig" | wc -l)"
        if [ $applied != $configured ];then
            p="$(fgrep -f "$src" "${cfg}/defconfig" | fgrep -vf - "$src" | \
                    sed 's/^# //;s/[= ].*$//')"
            for pat in $p;do
                bbwarn "'$(fgrep "$pat" "$src")' is obsolete in '$(basename "$src")'"
            done
        fi
    fi
}

do_check_config() {
    bbnote "Checking defconfig consistency"
    if [ -n "${UBOOT_CONFIG}" ];then
        i=0
        for config in ${UBOOT_MACHINE};do
            i=$(expr $i + 1)
            c="${B}/${config}"
            cp -v "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${c}/.config"
            if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
                merge_config.sh -m -r -O "${c}" "${c}/.config" "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}"
            fi
            merge_config.sh -m -r -O "${c}" "${c}/.config" "${WORKDIR}/u-boot-cfg.${MACHINE}"
            j=0
            for type in ${UBOOT_CONFIG};do
                j=$(expr $j + 1)
                [ $j = $i ] || continue
                if [ -s "${WORKDIR}/u-boot-cfg.${type}" ];then
                    bbnote "Appending '$type' specific config to '$(basename "${c}")/.config'"
                    merge_config.sh -m -r -O "${c}" "${c}/.config" "${WORKDIR}/u-boot-cfg.${type}"
                    check_cnf "${WORKDIR}/u-boot-cfg.${type}" "${c}"
                fi
                break
            done
            oe_runmake -C "${c}" olddefconfig
            check_cnf "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${c}"
            if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
                check_cnf "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" "${c}"
            fi
            check_cnf "${WORKDIR}/u-boot-cfg.${MACHINE}" "${c}"
        done
    else
        cp -v "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${B}/.config"
        if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
            merge_config.sh -m -r -O "${B}" "${B}/.config" "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}"
        fi
        merge_config.sh -m -r -O "${B}" "${B}/.config" "${WORKDIR}/u-boot-cfg.${MACHINE}"
        check_cnf "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${B}"
        if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
            check_cnf "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" "${B}"
        fi
        check_cnf "${WORKDIR}/u-boot-cfg.${MACHINE}" "${B}"
    fi
}
addtask do_check_config after do_savedefconfig
do_check_config[nostamp] = "1"

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

python do_env_overlays () {
    import os
    import shutil

    if d.getVar('KARO_BASEBOARDS') == None:
        bb.warn("KARO_BASEBOARDS is undefined")
        return 1
    if d.getVar('UBOOT_ENV_FILE') == None:
        bb.warn("UBOOT_ENV_FILE is undefined")
        return 1
    src_file = "%s/%s/%s.env" % (d.getVar('S'), d.getVar('UBOOT_BOARD_DIR'), d.getVar('UBOOT_ENV_FILE'))
    dst_dir = "%s/%s_config/%s" % (d.getVar('B'), d.getVar('MACHINE'), d.getVar('UBOOT_BOARD_DIR'))
    bb.utils.mkdirhier(dst_dir)
    env_file = os.path.join(dst_dir, os.path.basename(src_file))
    shutil.copyfile(src_file, env_file)
    f = open(env_file, 'a')
    for baseboard in d.getVar('KARO_BASEBOARDS').split():
        ovlist = d.getVarFlag('KARO_DTB_OVERLAYS', baseboard, True)
        if ovlist == None:
            bb.note("No overlays defined for '%s' on baseboard '%s'" % (d.getVar('MACHINE'), baseboard))
            continue
        overlays = " ".join(map(lambda f: f, ovlist.split()))
        bb.note("Adding overlays_%s='%s' to %s" % (baseboard, overlays, env_file))
        f.write("overlays_%s=%s\n" %(baseboard, overlays))
    f.write("soc_prefix=%s\n" % (d.getVar('SOC_PREFIX') or ""))
    f.write("soc_family=%s\n" % (d.getVar('SOC_FAMILY') or ""))
    f.close()
}
addtask do_env_overlays before do_compile after do_configure

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx93)"
