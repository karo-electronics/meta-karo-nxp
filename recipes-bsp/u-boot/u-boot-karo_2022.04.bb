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

inherit use-imx-security-controller-firmware

require recipes-bsp/u-boot/u-boot.inc
require conf/machine/include/${SOC_PREFIX}-overlays.inc
inherit fsl-u-boot-localversion

FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}/defconfigs:${THISDIR}/${BP}/cfg:${THISDIR}/${BP}/env:"

PROVIDES += "u-boot"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

#UBOOT_SRC ?= "git://github.com/karo-electronics/karo-tx-uboot.git;protocol=https"
UBOOT_SRC ?= "git:///net/karonas/repos/git/u-boot"
SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH}"
#SRCBRANCH = "lf_v2022.04-karo"
#SRCREV = "cb7fe6fb13f8258dacc1ac0ee62c7173a7644309"
SRCBRANCH = "lf_v2022.04-devel"
SRCREV = "1372944df3fea40cacd5b0bfdf8585021d5cc959"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

LOCALVERSION = "-${SRCBRANCH}-karo"

IMX_EXTRA_FIRMWARE:mx8m-nxp-bsp = "firmware-imx-8m"
IMX_EXTRA_FIRMWARE:mx9-nxp-bsp = "firmware-imx-9 firmware-sentinel"

ATF_MACHINE_NAME ?= "bl31-${ATF_PLATFORM}.bin"
ATF_MACHINE_NAME:append = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', '-optee', '', d)}"

DEPENDS += " \
    ${IMX_EXTRA_FIRMWARE} \
    imx-atf \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os', '', d)} \
"
DEPENDS:append:mx8m-nxp-bsp = " u-boot-mkimage-native"

UBOOT_BOARD_DIR:mx8-nxp-bsp = "board/karo/tx8m"
UBOOT_BOARD_DIR:mx9-nxp-bsp = "board/karo/imx93"

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
SRC_URI:append = " \
    file://dts/${DTB_BASENAME}-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/${DTB_BASENAME}.dts;subdir=git/arch/arm \
"

SRC_URI:append = " file://u-boot-cfg.${SOC_PREFIX}"
SRC_URI:append = " file://u-boot-cfg.${SOC_FAMILY}"
SRC_URI:append = " file://u-boot-cfg.${MACHINE}"
SRC_URI:append = "${@ "".join(map(lambda f: " file://u-boot-cfg.%s" % f, d.getVar('UBOOT_CONFIG').split()))}"

EXTRA_OEMAKE:append = " V=0"

FILES:${PN} += "${@ "".join(map(lambda f: " u-boot-%s-%s.%s" % (d.getVar('MACHINE'), \
                                f, d.getVar('UBOOT_SUFFIX')), \
                                d.getVar('UBOOT_CONFIG').split()))}"

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
                bbnote "Copying 'u-boot-cfg.${SOC_PREFIX}' to 'configs/${c}'"
                cp "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${S}/configs/${c}"
                if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
                    bbnote "Appending 'u-boot-cfg.${SOC_FAMILY}' to 'configs/${c}'"
                    cat "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" >> "${S}/configs/${c}"
                fi
                bbnote "Appending 'u-boot-cfg.${MACHINE}' to 'configs/${c}'"
                cat "${WORKDIR}/u-boot-cfg.${MACHINE}" >> "${S}/configs/${c}"
                if [ -s "${WORKDIR}/u-boot-cfg.${type}" ];then
                    bbnote "Appending 'u-boot-cfg.${type}' to 'configs/${c}'"
                    cat "${WORKDIR}/u-boot-cfg.${type}" >> "${S}/configs/${c}"
                fi
                break
            done
        done
        unset i j
    else
        c="${MACHINE}_defconfig"
        bbnote "Copying 'u-boot-cfg.${SOC_PREFIX}' to 'configs/${c}'"
        cp "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${S}/configs/${c}"
        if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
            bbnote "Appending 'u-boot-cfg.${SOC_FAMILY}' to 'configs/${c}'"
            cat "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" >> "${S}/configs/${c}"
        fi
        if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
            bbnote "Appending 'u-boot-cfg.${SOC_FAMILY}' to 'configs/${c}'"
            cat "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" >> "${S}/configs/${c}"
        fi
        bbnote "Appending 'u-boot-cfg.${MACHINE}' to 'configs/${c}'"
        cat "${WORKDIR}/u-boot-cfg.${MACHINE}" >> "${S}/configs/${c}"
    fi
}

do_configure:append() {
    tmpfile="`mktemp cfg-XXXXXX.tmp`"
    if [ -z "$tmpfile" ];then
        bbfatal "Failed to create tmpfile"
    fi
    if [ -n "${KARO_BASEBOARD}" ];then
        cat <<EOF >> "$tmpfile"
CONFIG_DEFAULT_DEVICE_TREE="${DTB_BASENAME}-${KARO_BASEBOARD}"
EOF
        grep -q "${DTB_BASENAME}-${KARO_BASEBOARD}\.dtb" ${S}/arch/arm/dts/Makefile || \
                sed -i '/^targets /i\
dtb-y += ${DTB_BASENAME}-${KARO_BASEBOARD}.dtb\
' ${S}/arch/arm/dts/Makefile
        echo "CONFIG_OF_LIST=\"${DTB_BASENAME}-${KARO_BASEBOARD} ${DTB_BASENAME}\"" >> "$tmpfile"
    fi

    bbnote "UBOOT_ENV_FILE='${UBOOT_ENV_FILE}'"
    if [ -n "${UBOOT_ENV_FILE}" ];then
        cat <<EOF >> "$tmpfile"
CONFIG_USE_DEFAULT_ENV_FILE=y
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
            for feature in ${UBOOT_FEATURES};do
                bbnote "Appending '$feature' specific config to '$(basename "${c}")/.config'"
                merge_config.sh -m -r -O "${c}" "${c}/.config" "${WORKDIR}/${feature}.cfg"
                check_cnf "${WORKDIR}/${feature}.cfg" "${c}"
            done
            check_cnf "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${c}"
            if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
                check_cnf "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" "${c}"
            fi
            check_cnf "${WORKDIR}/u-boot-cfg.${MACHINE}" "${c}"

            # restore the original config
            cp -v "${c}/defconfig" "${c}/.config"
            oe_runmake -C "${c}" olddefconfig
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

        # restore the original config
        cp -v "${B}/defconfig" "${B}/.config"
        oe_runmake -C "${B}" olddefconfig
    fi
}
addtask do_check_config after do_savedefconfig
do_check_config[nostamp] = "1"

# This package aggregates output deployed by other packages,
# so set the appropriate dependencies
do_compile[depends] += " \
    ${@ "".join(map(lambda f: " %s:do_deploy" % f, d.getVar('IMX_EXTRA_FIRMWARE').split()))} \
    imx-atf:do_deploy \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os:do_deploy', '', d)} \
"

do_compile:prepend() {
    if [ -n "${UBOOT_CONFIG}" ];then
	for m in ${UBOOT_MACHINE};do
	    bbnote "Copy ATF: '${ATF_MACHINE_NAME}' from '${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}' to '${B}/${m}/bl31.bin'"
	    install -v "${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${ATF_MACHINE_NAME}" "${B}/${m}/bl31.bin"
	    for f in ${DDR_FIRMWARE_NAME} ${SECO_FIRMWARE_NAME};do
		bbnote "Copy ddr_firmware: ${f} from ${DEPLOY_DIR_IMAGE} -> ${B}/${m}"
		install -D "${DEPLOY_DIR_IMAGE}/${f}" "${B}/${m}"
	    done
	done
    else
	bbnote "Copy ATF: '${ATF_MACHINE_NAME}' from '${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}' to '${B}'"
	install -v "${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${ATF_MACHINE_NAME}" "${B}/bl31.bin"
	for f in ${DDR_FIRMWARE_NAME} ${SECO_FIRMWARE_NAME};do
	    bbnote "Copy ddr_firmware: ${f} from ${DEPLOY_DIR_IMAGE} -> ${B}"
	    install -D "${DEPLOY_DIR_IMAGE}/${f}" "${B}"
	done
    fi
}

do_deploy:append:mx8m-nxp-bsp () {
    install -d "${DEPLOYDIR}"

    if [ -n "${UBOOT_CONFIG}" ];then
        i=0
        for config in ${UBOOT_MACHINE};do
            i=$(expr $i + 1)
            j=0
            for type in ${UBOOT_CONFIG};do
                j=$(expr $j + 1)
                if [ $j -eq $i ];then
                    install -m 0644 "${B}/${config}/flash.bin" "${DEPLOYDIR}/u-boot-${MACHINE}-${type}.${UBOOT_SUFFIX}"
                fi
            done
            unset j
        done
        unset i
    else
        install -m 0644 "${B}/flash.bin" "${DEPLOYDIR}/u-boot-${MACHINE}.${UBOOT_SUFFIX}"
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

python do_env_overlays () {
    import os
    import shutil

    if d.getVar('KARO_BASEBOARDS') == None:
        bb.warn("KARO_BASEBOARDS is undefined")
        return 1
    if d.getVar('UBOOT_ENV_FILE') == None:
        bb.warn("UBOOT_ENV_FILE is undefined")
        return 1

    overlays = []
    for baseboard in d.getVar('KARO_BASEBOARDS').split():
        ovlist = d.getVarFlag('KARO_DTB_OVERLAYS', baseboard, True)
        if ovlist == None:
            bb.note("No overlays defined for '%s' on baseboard '%s'" % (d.getVar('MACHINE'), baseboard))
            continue
        ov = " ".join(map(lambda f: f, ovlist.split()))
        overlays += ["overlays_%s=%s" % (baseboard, ov)]

    src_file = "%s/%s/%s.env" % (d.getVar('S'), d.getVar('UBOOT_BOARD_DIR'), d.getVar('UBOOT_ENV_FILE'))
    if d.getVar('UBOOT_CONFIG') != None:
        configs = d.getVar('UBOOT_MACHINE').split()
    else:
        configs = (d.getVar('MACHINE'))

    for config in configs:
        dst_dir = "%s/%s/%s" % (d.getVar('B'), config, d.getVar('UBOOT_BOARD_DIR'))
        bb.utils.mkdirhier(dst_dir)
        env_file = os.path.join(dst_dir, os.path.basename(src_file))
        shutil.copyfile(src_file, env_file)
        f = open(env_file, 'a')
        for ov in overlays:
            bb.note("Adding '%s' to '%s'" % (ov, env_file))
            f.write("%s\n" % ov)
        f.write("soc_prefix=%s\n" % (d.getVar('SOC_PREFIX') or ""))
        f.write("soc_family=%s\n" % (d.getVar('SOC_FAMILY') or ""))
        f.close()
}
addtask do_env_overlays before do_compile after do_configure
do_env_overlays[vardeps] += "KARO_BASEBOARDS KARO_DTB_OVERLAYS"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx8m-nxp-bsp|mx93)"
