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

# upstream source
#KARO_UBOOT_SRC = "git://source.denx.de/u-boot/u-boot.git"
#UBOOT_REV = "e37de002fac3895e8d0b60ae2015e17bb33e2b5b"
#UBOOT_BRANCH = "master"

# local source
KARO_UBOOT_SRC ?= "git://github.com/karo-electronics/karo-tx-uboot.git;protocol=https"
UBOOT_SRC = "${KARO_UBOOT_SRC}"
UBOOT_REV ?= "a979640c90604697f8999dceb507f2aee0a1bb3a"
UBOOT_BRANCH ?= "u-boot-denx"

SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH}"
SRCBRANCH = "${UBOOT_BRANCH}"
SRCREV = "${UBOOT_REV}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

LOCALVERSION = "-karo"

ATF_MACHINE_NAME ?= "bl31-${ATF_PLATFORM}.bin"
ATF_MACHINE_NAME:append = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', '-optee', '', d)}"

DEPENDS += " \
    ${IMX_EXTRA_FIRMWARE} \
    imx-atf \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os', '', d)} \
"
DEPENDS:append = " u-boot-mkimage-native"

UBOOT_BOARD_DIR:mx8-nxp-bsp = "board/karo/tx8m"
UBOOT_BOARD_DIR:mx93-nxp-bsp = "board/karo/imx93"
UBOOT_BOARD_DIR:mx91-nxp-bsp = "board/karo/imx91"

UBOOT_ENV_FILE ?= "${@ "%s%s" % (d.getVar('MACHINE'), \
                       "-" + d.getVar('KARO_BASEBOARD') \
                       if d.getVar('KARO_BASEBOARD') != "" else "")}"

SRC_URI:append = "${@ "".join(map(lambda f: " file://%s.cfg" % f, d.getVar('UBOOT_FEATURES').split()))}"

SRC_URI:append = "${@ " file://%s.env;subdir=git/%s" % \
                      (d.getVar('UBOOT_ENV_FILE'), d.getVar('UBOOT_BOARD_DIR')) \
                      if d.getVar('UBOOT_ENV_FILE') != None else ""} \
"

SRC_URI:append = " ${@ " file://dts/%s.dts;subdir=git/arch/arm" %  d.getVar('UBOOT_DTB_NAME')}"
SRC_URI:append = " ${@ " file://dts/%s-u-boot.dtsi;subdir=git/arch/arm" % d.getVar('UBOOT_DTB_NAME')}"
SRC_URI:append = " \
    file://dts/${DTB_BASENAME}-u-boot.dtsi;subdir=git/arch/arm \
    file://dts/${DTB_BASENAME}.dts;subdir=git/arch/arm \
"

SRC_URI:append = " file://u-boot-cfg.${SOC_PREFIX}"
SRC_URI:append = " file://u-boot-cfg.${SOC_FAMILY}"
SRC_URI:append = " file://u-boot-cfg.${MACHINE}"
SRC_URI:append = "${@ "".join(map(lambda f: " file://u-boot-cfg.%s" % f, d.getVar('UBOOT_CONFIG').split()))}"
SRC_URI:append = "${@ bb.utils.contains('IMAGE_INSTALL', 'u-boot-fw-utils', " file://fw_env.config", "", d)}"

EXTRA_OEMAKE:append = " V=0"
EXTRA_OEMAKE:append = " BL31_BASE=${ATF_BL31_BASE} BL31_SIZE=${ATF_BL31_SIZE}"
EXTRA_OEMAKE:append = " UBOOT_CFG_MALLOC_F_ADDR=${UBOOT_CFG_MALLOC_F_ADDR}"

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

do_configure:prepend() {
    bbnote "SRC_URI='${SRC_URI}'"
    if [ -n "${UBOOT_CONFIG}" ];then
        i=0
        for config in ${UBOOT_MACHINE};do
            i=$(expr $i + 1)
            j=0
            for type in ${UBOOT_CONFIG};do
                j=$(expr $j + 1)
                [ $j = $i ] || continue
                c="`echo "$config" | sed 's/_config/_defconfig/'`"
                bbnote "Copying 'u-boot-cfg.${SOC_PREFIX}' to 'configs/${c}'"
                cp "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${S}/configs/${c}"
                grep 'SPL_.*STACK' "${S}/configs/${c}"
                if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
                    bbnote "Appending 'u-boot-cfg.${SOC_FAMILY}' to 'configs/${c}'"
                    cat "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" >> "${S}/configs/${c}"
                    grep 'SPL_.*STACK' "${S}/configs/${c}"
                fi
                bbnote "Appending 'u-boot-cfg.${MACHINE}' to 'configs/${c}'"
                cat "${WORKDIR}/u-boot-cfg.${MACHINE}" >> "${S}/configs/${c}"
                if [ -s "${WORKDIR}/u-boot-cfg.${type}" ];then
                    bbnote "Appending 'u-boot-cfg.${type}' to 'configs/${c}'"
                    cat "${WORKDIR}/u-boot-cfg.${type}" >> "${S}/configs/${c}"
                    grep 'SPL_.*STACK' "${S}/configs/${c}"
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
            grep 'SPL_.*STACK' "${S}/configs/${c}"
        fi
        if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
            bbnote "Appending 'u-boot-cfg.${SOC_FAMILY}' to 'configs/${c}'"
            cat "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" >> "${S}/configs/${c}"
            grep 'SPL_.*STACK' "${S}/configs/${c}"
        fi
        bbnote "Appending 'u-boot-cfg.${MACHINE}' to 'configs/${c}'"
        cat "${WORKDIR}/u-boot-cfg.${MACHINE}" >> "${S}/configs/${c}"
        grep 'SPL_.*STACK' "${S}/configs/${c}"
    fi
    (cd ${S}; egrep '(TARGET|CONFIG)_KARO_' configs/${MACHINE}*config)
    (cd ${S}; grep 'SPL_.*STACK' configs/${MACHINE}*config)
}

do_configure:append() {
    (cd ${B}; egrep '(TARGET|CONFIG)_KARO_' */.config)
    tmpfile="`mktemp cfg-XXXXXX.tmp`"
    if [ -z "$tmpfile" ];then
        bbfatal "Failed to create tmpfile"
    fi
    add_conf() {
        [ $# = 2 ] || return 0
        echo "$1=$2" >> "$tmpfile"
    }
    add_conf CONFIG_BL31_BASE ${ATF_BL31_BASE}
    add_conf CONFIG_DEFAULT_DEVICE_TREE "\"${UBOOT_DTB_NAME}\""

    if [ -n "${KARO_BASEBOARD}" ];then
        add_conf CONFIG_OF_LIST "\"${UBOOT_DTB_NAME} ${DTB_BASENAME}\""
    else
        add_conf CONFIG_OF_LIST "\"${DTB_BASENAME}\""
    fi

    bbnote "UBOOT_ENV_FILE='${UBOOT_ENV_FILE}'"
    if [ -n "${UBOOT_ENV_FILE}" ];then
        add_conf CONFIG_USE_DEFAULT_ENV_FILE y
        add_conf CONFIG_DEFAULT_ENV_FILE "\"board/\$(VENDOR)/\$(BOARD)/${UBOOT_ENV_FILE}.env\""
    else
        echo "# CONFIG_USE_DEFAULT_ENV_FILE is not set" >> "$tmpfile"
    fi

    # convey common settings for imx-atf and U-Boot to U-Boot config
    add_conf CONFIG_SAVED_DRAM_TIMING_BASE ${UBOOT_SAVED_DRAM_TIMING_BASE}
    add_conf CONFIG_SPL_STACK              ${UBOOT_SPL_STACK}
    add_conf CONFIG_SPL_TEXT_BASE          ${UBOOT_SPL_TEXT_BASE}
    add_conf CONFIG_SPL_BSS_START_ADDR     ${UBOOT_SPL_BSS_START_ADDR}

    if [ -n "${UBOOT_CONFIG}" ];then
        for config in ${UBOOT_MACHINE};do
            c="${B}/${config}"
	    echo "before ${config} olddefconfig"
	    grep 'SPL_.*STACK' "${c}/.config"
            oe_runmake -C ${c} olddefconfig
	    echo "after ${config} olddefconfig"
	    grep 'SPL_.*STACK' "${c}/.config"
            merge_config.sh -m -r -O "${c}" "${c}/.config" "$tmpfile"
	    echo "after ${config} mergeconfig"
	    grep 'SPL_.*STACK' "${c}/.config"
        done
    else
        c=${B}
        oe_runmake -C "${B}" olddefconfig
        merge_config.sh -m -r -O "${B}" "${B}/.config" "$tmpfile"
    fi
    rm -vf "$tmpfile"
    if ${@ bb.utils.contains('DISTRO_FEATURES', 'u-boot-fw-utils', "true", "false", d)};then
        env_offset=$(sed -n '/^CONFIG_ENV_OFFSET=/{s/^.*=//;p}' ${c}/.config)
        env_size=$(sed -n '/^CONFIG_ENV_SIZE=/{s/^.*=//;p}' ${c}/.config)
        cat > ${WORKDIR}/fw_env.config <<EOF
/dev/mmcblk0boot0	$env_offset	$env_size
EOF
    fi
}

check_cnf() {
    local src="$1"
    local cfg="$2"
    fgrep -f "$src" "${cfg}/.config" || true
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
                    check_cnf "${WORKDIR}/u-boot-cfg.${type}" "${c}"
                fi
                break
            done
            check_cnf "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${c}"
            if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
                check_cnf "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" "${c}"
            fi
            check_cnf "${WORKDIR}/u-boot-cfg.${MACHINE}" "${c}"

            for feature in ${UBOOT_FEATURES};do
                bbnote "Appending '$feature' specific config to '$(basename "${c}")/.config'"
                merge_config.sh -m -r -O "${c}" "${c}/.config" "${WORKDIR}/${feature}.cfg"
                check_cnf "${WORKDIR}/${feature}.cfg" "${c}"
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
        check_cnf "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${B}"
        if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
            check_cnf "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" "${B}"
        fi
        check_cnf "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" "${B}"
        check_cnf "${WORKDIR}/u-boot-cfg.${MACHINE}" "${B}"
        for feature in ${UBOOT_FEATURES};do
            bbnote "Appending '$feature' specific config to '.config'"
            merge_config.sh -m -r -O "${c}" "${B}/.config" "${WORKDIR}/${feature}.cfg"
            check_cnf "${WORKDIR}/${feature}.cfg" "${B}"
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

uboot_install_config () {
    config=$1
    type=$2

    if [ -z "${UBOOT_INITIAL_ENV}" ]; then
        return
    fi
    if [ "$type" != ${@ d.getVarFlag('UBOOT_CONFIG', 'default')}" ];then
        return
    fi
    install -D -m 644 ${B}/${config}/u-boot-initial-env-${type} ${D}/${sysconfdir}/${UBOOT_INITIAL_ENV}
}

do_deploy:append () {
    if [ -n "${UBOOT_CONFIG}" ];then
        i=0
        for config in ${UBOOT_MACHINE};do
            i=$(expr $i + 1)
            j=0
            for type in ${UBOOT_CONFIG};do
                j=$(expr $j + 1)
                [ $j = $i ] || continue
                install -v "${B}/${config}/flash.bin" "u-boot-${MACHINE}-${type}.${UBOOT_SUFFIX}"
                break
            done
            unset j
        done
        unset i
    else
        install -v "${B}/flash.bin" "u-boot-${MACHINE}.${UBOOT_SUFFIX}"
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

do_updatedefconfig() {
    if [ -n "${UBOOT_CONFIG}" ];then
        for config in ${UBOOT_MACHINE};do
            defconfig="`echo "$config" | sed 's/_config/_defconfig/'`"
            bbplain "Saving defconfig to:\n${S}/configs/${defconfig}"
            cp -av ${B}/${config}/defconfig ${S}/configs/${defconfig}
        done
    else
        bbplain "Saving defconfig to:\n${S}/configs/${MACHINE}_defconfig"
        cp -av ${B}/${config}/defconfig ${S}/configs/${MACHINE}_defconfig
    fi
}
addtask updatedefconfig after do_savedefconfig

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
        if config.find('mfg_config') > 0:
            f.write("preboot=fastboot 0\n")
        f.write("soc_prefix=%s\n" % (d.getVar('SOC_PREFIX') or ""))
        f.write("soc_family=%s\n" % (d.getVar('SOC_FAMILY') or ""))
        f.close()
}
addtask do_env_overlays before do_compile after do_configure
do_env_overlays[vardeps] += "KARO_BASEBOARDS KARO_DTB_OVERLAYS"

uboot_install_config() {
    config=$1
    type=$2

    # Install the uboot-initial-env
    if [ -n "${UBOOT_INITIAL_ENV}" -a "$type" = "default" ]; then
        install -D -m 644 ${B}/${config}/u-boot-initial-env-${type} ${D}${sysconfdir}/${UBOOT_INITIAL_ENV}
    fi
}

uboot_install_spl_config() {
    : nothing to be seen here
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx8m-nxp-bsp|mx9-nxp-bsp)"
