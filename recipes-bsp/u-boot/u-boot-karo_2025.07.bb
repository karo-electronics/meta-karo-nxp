# Copyright (C) 2023 Lothar Waßmann <LW@KARO-electronics.de>
# based on: meta-imx/meta-bsp/recipes-bsp/u-boot/u-boot-imx_2022.04.bb
# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2018 (C) O.S. Systems Software LTDA.
# Copyright 2017-2022 NXP

DESCRIPTION = "i.MX U-Boot suppporting Ka-Ro electronics boards."
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS += "\
    bc-native \
    bison-native \
    dtc-native \
    flex-native \
    gnutls-native \
    xxd-native \
    python3-setuptools-native \
    ${IMX_EXTRA_FIRMWARE} \
    imx-atf \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os', '', d)} \
    u-boot-mkimage-native \
"
# No ATF on i.MX6.
DEPENDS:remove:mx6-nxp-bsp = "imx-atf"

require recipes-bsp/u-boot/u-boot.inc
require u-boot-karo-configure.inc
inherit ${@ "" if "mx6-nxp-bsp" in d.getVar("MACHINEOVERRIDES").split(":") else "use-imx-security-controller-firmware"}
inherit fsl-u-boot-localversion
LOCALVERSION = "-karo"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}/defconfigs:${THISDIR}/${BP}/cfg:${THISDIR}/${BP}/env:"

PROVIDES += "u-boot"

UBOOT_SRC_DEFAULT = "git://github.com/karo-electronics/karo-tx-uboot.git;protocol=https"
UBOOT_REV_DEFAULT = "c9f00792446a4cc343e08e22f2230e0af2d8831c"
UBOOT_BRANCH_DEFAULT = "u-boot-denx"

CVE_PRODUCT = "denx:u-boot"
CVE_VERSION = "2025.07"

KARO_UBOOT_SRC ?= "${UBOOT_SRC_DEFAULT}"
KARO_UBOOT_REV ?= "${UBOOT_REV_DEFAULT}"
KARO_UBOOT_BRANCH ?= "${UBOOT_BRANCH_DEFAULT}"

UBOOT_SRC = "${KARO_UBOOT_SRC}"
UBOOT_REV = "${KARO_UBOOT_REV}"
UBOOT_BRANCH = "${KARO_UBOOT_BRANCH}"

SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH}"
SRCBRANCH = "${UBOOT_BRANCH}"
SRCREV = "${UBOOT_REV}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

ATF_MACHINE_NAME ?= "bl31-${ATF_PLATFORM}.bin"
ATF_MACHINE_NAME:append = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', '-optee', '', d)}"

UBOOT_BOARD_DIR:mx8-nxp-bsp = "board/karo/tx8m"
UBOOT_BOARD_DIR:mx93-nxp-bsp = "board/karo/imx93"
UBOOT_BOARD_DIR:mx91-nxp-bsp = "board/karo/imx91"
UBOOT_BOARD_DIR:mx6-nxp-bsp = "board/karo/imx6"

# Carriers whose control DT goes into the U-Boot FIT.
KARO_UBOOT_BASEBOARDS ?= "${KARO_BASEBOARDS}"
# The runtime selection picks among these, module DT first.
KARO_UBOOT_OF_LIST = "${@ ' '.join([d.getVar('DTB_BASENAME')] + ['%s-%s' % (d.getVar('DTB_BASENAME'), b) for b in (d.getVar('KARO_UBOOT_BASEBOARDS') or '').split()])}"

KARO_FW_ENV_MMC ?= "/dev/mmcblk0boot0"
KARO_FW_ENV_MMC:mx6q-nxp-bsp = "/dev/mmcblk3boot0"
KARO_FW_ENV_MMC:mx6dl-nxp-bsp = "/dev/mmcblk3boot0"

KARO_FW_ENV_NAND ?= "/dev/mtd0"
KARO_FW_ENV_NAND:mx6-nxp-bsp = "/dev/mtd1"

UBOOT_FEATURES:append = "${@ " ksz9x-phy" if d.getVar('KARO_BASEBOARD') in "qsbase1 qsbase4".split() else ""}"
UBOOT_FEATURES:append = "${@ " pmic-${KARO_BOARD_PMIC}" if "${KARO_BOARD_PMIC}" != "" else ""}"
UBOOT_FEATURES:append = "${@ bb.utils.contains('MACHINE_FEATURES', "optee", " optee", "", d)}"
UBOOT_FEATURES:append = "${@ bb.utils.contains('DISTRO_FEATURES', "copro", " copro", "", d)}"
UBOOT_FEATURES:append = "${@ bb.utils.contains('DISTRO_FEATURES', "rauc", " rauc", "", d)}"
UBOOT_FEATURES:append = "${@ bb.utils.contains('MACHINE_FEATURES', "nand", " ubi", "", d)}"
UBOOT_FEATURES:append = " fastboot"

UBOOT_ENV_FILE ?= "${@ "%s%s" % (d.getVar('MACHINE'), \
                       "-" + d.getVar('KARO_BASEBOARD') \
                       if d.getVar('KARO_BASEBOARD') != "" else "")}"

SRC_URI:append = "${@ " file://${UBOOT_ENV_FILE}.env" if d.getVar('UBOOT_ENV_FILE') != None else ""}"
SRC_URI:append = "${@ bb.utils.contains('DISTRO_FEATURES', 'rauc', " file://rauc.env", "", d)}"

SRC_URI:append = " \
    file://dts/${UBOOT_DTB_NAME}.dts;subdir=git/arch/arm \
    file://dts/${UBOOT_DTB_NAME}-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/${DTB_BASENAME}.dts;subdir=git/arch/arm \
    file://dts/${DTB_BASENAME}-u-boot.dtsi;subdir=git/arch/arm \
"

SRC_URI:append = " file://u-boot-cfg.${SOC_PREFIX}"
SRC_URI:append = " file://u-boot-cfg.${SOC_FAMILY}"
SRC_URI:append = " file://u-boot-cfg.${MACHINE}"
SRC_URI:append = "${@ "".join(map(lambda f: " file://u-boot-cfg.%s" % f, d.getVar('UBOOT_CONFIG').split()))}"
SRC_URI:append = "${@ "".join(map(lambda f: " file://%s.cfg" % f, d.getVar('UBOOT_FEATURES').split()))}"

EXTRA_OEMAKE:append = " V=0"

FILES:${PN} += "${@ "".join(map(lambda f: " u-boot-%s-%s.%s" % (d.getVar('MACHINE'), \
                                f, d.getVar('UBOOT_SUFFIX')), \
                                d.getVar('UBOOT_CONFIG').split()))}"

do_fetch[prefuncs] =+ "karo_check_baseboard"

python karo_check_baseboard () {
    bb.note("Checking validity of KARO_BASEBOARD: ")
    if d.getVar('KARO_BASEBOARDS') == None:
        bb.fatal("'KARO_BASEBOARDS' is undefined")
    if d.getVar('KARO_BASEBOARD') != "":
        if d.getVar('KARO_BASEBOARD') not in d.getVar('KARO_BASEBOARDS').split():
            raise_sanity_error("Module %s is not supported on Baseboard '%s'; \
                               available baseboards are:\n%s" % \
                               (d.getVar('MACHINE'), d.getVar('KARO_BASEBOARD'), \
                               "\n".join(d.getVar('KARO_BASEBOARDS').split())), d)
}

# Verify that a config fragment is accepted by kconfig
karo_check_config() {
    local cfg="$1"
    shift
    fgrep -f "$@" "${cfg}/.config" || true
    local applied=$(cat "$@" | fgrep -f - "${cfg}/.config" | wc -l)
    local configured=$(cat "$@" | wc -l)
    if [ $applied != $configured ];then
        local p="$(cat "$@" | fgrep -f - "${cfg}/.config" | fgrep -vhf - "$@" | \
                sed 's/^# //;s/[= ].*$//')"
        local pat
        for pat in $p;do
            for src in "$@";do
                grep -q "$pat[ =]" "${cfg}/.config" || continue
                bbwarn "Item '$pat' from $(basename "$src") has not been accepted by kconfig"
                if grep -q "${pat}[ =]" "${cfg}/.config";then
                    bbwarn "Actual Kconfig value of '$pat' is: '$(grep "$pat" "${cfg}/.config")'"
                else
                    bbwarn "'$pat' is not present in '$(basename "$cfg")/.config"
                fi
            done
        done
        applied="$(fgrep -f "$src" "${cfg}/defconfig" | wc -l)"
        if [ $applied != $configured ];then
            p="$(cat "$@" | fgrep -f - "${cfg}/defconfig" | fgrep -vhf - "$@" | \
                    sed 's/^# //;s/[= ].*$//')"
            for pat in $p;do
                bbwarn "'$(fgrep "$pat" "$@")' is obsolete in '$(basename `grep -l "${pat}[ =]" "$@"`)'"
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
            oe_runmake -C ${c} olddefconfig
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
                    karo_check_config "${c}" "${WORKDIR}/u-boot-cfg.${type}"
                fi
                break
            done
            if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
                karo_check_config "${c}" "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" \
                                 "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}"
            else
                karo_check_config "${c}" "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}"
            fi
            karo_check_config "${c}" "${WORKDIR}/u-boot-cfg.${MACHINE}"
            for feature in ${UBOOT_FEATURES};do
                bbnote "Appending '$feature' specific config to '$(basename "${c}")/.config'"
                merge_config.sh -m -r -O "${c}" "${c}/.config" "${WORKDIR}/${feature}.cfg"
                karo_check_config "${c}" "${WORKDIR}/${feature}.cfg"
            done

            # restore the original config
            cp -v "${c}/defconfig" "${c}/.config"
            oe_runmake -C "${c}" olddefconfig
        done
    else
        cp -v "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${B}/.config"
        oe_runmake -C ${B} olddefconfig
        if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
            merge_config.sh -m -r -O "${B}" "${B}/.config" "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}"
        fi
        merge_config.sh -m -r -O "${B}" "${B}/.config" "${WORKDIR}/u-boot-cfg.${MACHINE}"
        oe_runmake -C ${B} olddefconfig
        if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
            karo_check_config "${B}" "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" \
                             "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}"
        else
            karo_check_config "${B}" "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}"
        fi
        karo_check_config "${B}" "${WORKDIR}/u-boot-cfg.${MACHINE}"
        for feature in ${UBOOT_FEATURES};do
            bbnote "Appending '$feature' specific config to '.config'"
            merge_config.sh -m -r -O "${c}" "${B}/.config" "${WORKDIR}/${feature}.cfg"
            karo_check_config "${B}" "${WORKDIR}/${feature}.cfg"
        done

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
    ${@ "" if "mx6-nxp-bsp" in d.getVar("MACHINEOVERRIDES").split(":") else "imx-atf:do_deploy"} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os:do_deploy', '', d)} \
"

karo_copy_atf_firmware() {
    if [ -n "${UBOOT_CONFIG}" ];then
        for m in ${UBOOT_MACHINE};do
            bbnote "Copy ATF: '${ATF_MACHINE_NAME}' from '${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}' to '${B}/${m}/bl31.bin'"
            install -v "${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${ATF_MACHINE_NAME}" "${B}/${m}/bl31.bin"
            for f in ${DDR_FIRMWARE_NAME} ${SECO_FIRMWARE_NAME};do
                bbnote "Copy ddr_firmware: ${f} from ${DEPLOY_DIR_IMAGE} -> ${B}/${m}"
                install -v -D "${DEPLOY_DIR_IMAGE}/${f}" "${B}/${m}"
            done
        done
    else
        bbnote "Copy ATF: '${ATF_MACHINE_NAME}' from '${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}' to '${B}'"
        install -v "${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${ATF_MACHINE_NAME}" "${B}/bl31.bin"
        for f in ${DDR_FIRMWARE_NAME} ${SECO_FIRMWARE_NAME};do
            bbnote "Copy ddr_firmware: ${f} from ${DEPLOY_DIR_IMAGE} -> ${B}"
            install -v -D "${DEPLOY_DIR_IMAGE}/${f}" "${B}"
        done
    fi
}

karo_deploy_uboot_container() {
    if [ -n "${UBOOT_CONFIG}" ];then
        i=0
        for config in ${UBOOT_MACHINE};do
            i=$(expr $i + 1)
            j=0
            for type in ${UBOOT_CONFIG};do
                j=$(expr $j + 1)
                [ $j = $i ] || continue
                install -v "${B}/${config}/flash.bin" "${DEPLOYDIR}/u-boot-${MACHINE}-${type}.${UBOOT_SUFFIX}"
                break
            done
            unset j
        done
        unset i
    else
        install -v "${B}/flash.bin" "${DEPLOYDIR}/u-boot-${MACHINE}.${UBOOT_SUFFIX}"
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

do_updatedefconfig() {
    if [ -n "${KARO_BASEBOARD}" ];then
        mach="${MACHINE}-${KARO_BASEBOARD}"
    else
        mach="${MACHINE}"
    fi
    if [ -n "${UBOOT_CONFIG}" ];then
        i=0
        for config in ${UBOOT_MACHINE};do
            i=$(expr $i + 1)
            j=0
            for type in ${UBOOT_CONFIG};do
                j=$(expr $j + 1)
                [ $j = $i ] || continue
                if [ "$type" = "default" ];then
                    bbplain "Saving defconfig to:\n${S}/configs/${mach}_${type}_defconfig"
                    install -v "${B}/${config}/defconfig" "${S}/configs/${mach}_${type}_defconfig"
                else
                    bbplain "Saving defconfig to:\n${S}/configs/${mach}_${type}_defconfig"
                    install -v "${B}/${config}/defconfig" "${S}/configs/${mach}_${type}_defconfig"
                fi
                break
            done
            unset j
        done
        unset i
    else
        bbplain "Saving defconfig to:\n${S}/configs/${mach}_defconfig"
        install -v "${B}/defconfig" "${S}/configs/${mach}_defconfig"
    fi
}
addtask do_updatedefconfig after do_savedefconfig

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

        ovl = "overlays_%s=" % baseboard
        dlm = ""
        for ov in ovlist.split():
            ovf = ov.split(",")
            # omit alternatively used overlays
            if len(ovf) == 1:
                ovl += dlm + ovf[0]
                dlm = " "
        overlays += [ovl]

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

COMPATIBLE_MACHINE = "(mx6-nxp-bsp|mx8m-nxp-bsp|mx9-nxp-bsp)"

do_compile:prepend:mx8m-nxp-bsp() {
    karo_copy_atf_firmware
}

do_compile:prepend:mx9-nxp-bsp() {
    karo_copy_atf_firmware
}

do_deploy:append:mx8m-nxp-bsp() {
    karo_deploy_uboot_container
}

do_deploy:append:mx9-nxp-bsp() {
    karo_deploy_uboot_container
}

# i.MX6 has no boot container; deploy the monolithic image under the same
# names, which karo-nxp.inc expects.
do_deploy:append:mx6-nxp-bsp() {
    i=0
    for config in ${UBOOT_MACHINE};do
        i=$(expr $i + 1)
        j=0
        for type in ${UBOOT_CONFIG};do
            j=$(expr $j + 1)
            [ $j = $i ] || continue
            install -v "${B}/${config}/${UBOOT_BINARY}" \
                "${DEPLOYDIR}/u-boot-${MACHINE}-${type}.${UBOOT_SUFFIX}"
            break
        done
        unset j
    done
    unset i
}
