FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:${THISDIR}/${PN}/patches:${THISDIR}/${PN}/cfg:"

inherit fsl-u-boot-localversion
LOCALVERSION = "-karo"

SRC_URI:append = " \
    file://0001-fastboot-add-support-for-non-fsl-fastboot.patch \
    file://0002-board-add-board-specific-logic-and-for-qs95-module.patch \
    file://0003-configs-add-defconfigs-for-qs95-5610-module.patch \
    file://0004-dts-add-files-for-qs95-5610-module.patch \
    file://0005-board-add-qs95-specific-mac-readout-function.patch \
    file://0006-mmc-fsl_esdhc_imx-fail-HS400-setup-when-the-strobe-D.patch \
    file://0007-dts-qs95-use-a-documented-slew-rate-on-ENET-TXC-MDC-.patch \
    file://0008-dts-qs95-use-a-3.3-V-drive-strength-on-the-PHY-reset.patch \
"

UBOOT_FEATURES:append:qs95 = " fastboot"

SRC_URI:append = "${@ "".join(" file://%s.cfg" % f for f in (d.getVar('UBOOT_FEATURES') or "").split())}"

UBOOT_BOARD_DIR:qs95 = "board/karo/imx95"

do_fetch[prefuncs] =+ "karo_check_baseboard"

# A wrong baseboard name would otherwise build silently without carrier overlays
python karo_check_baseboard () {
    baseboards = d.getVar('KARO_BASEBOARDS')
    if baseboards is None:
        bb.fatal("'KARO_BASEBOARDS' is undefined")

    baseboard = d.getVar('KARO_BASEBOARD') or ""
    # an empty value is the module only build, without any carrier
    if baseboard and baseboard not in baseboards.split():
        bb.fatal("Module '%s' does not support baseboard '%s'; available baseboards are:\n%s" %
                 (d.getVar('MACHINE'), baseboard, "\n".join(baseboards.split())))
}

# Create symlink to fix path resolution error when using devtool
do_create_symlink() {
    if [ -n "${UBOOT_CONFIG}" -o -n "${UBOOT_DELTA_CONFIG}" ]
    then
        mkdir -p ${B}

        i=0
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            j=0
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                [ $j -eq $i ] || continue
                ln -sf ${B}/${config}-${type} ${B}/${config}
                break
            done
            unset j
        done
        unset i
    fi
}
addtask do_create_symlink before do_configure after do_unpack

# Adjust u-boot build config based on KARO_BASEBOARD
do_adjust_config() {
    tmpfile="`mktemp cfg-XXXXXX.tmp`"
    if [ -z "$tmpfile" ];then
        bbfatal "Failed to create tmpfile"
    fi
    add_conf() {
        [ $# = 2 ] || return 0
        echo "$1=$2" >> "$tmpfile"
    }

    add_conf CONFIG_DEFAULT_DEVICE_TREE "\"${UBOOT_DTB_NAME}\""

    if [ -n "${KARO_BASEBOARD}" ];then
        add_conf CONFIG_OF_LIST "\"${UBOOT_DTB_NAME} ${DTB_BASENAME}\""
    else
        add_conf CONFIG_OF_LIST "\"${DTB_BASENAME}\""
    fi

    add_conf CONFIG_ENV_USE_DEFAULT_ENV_TEXT_FILE y
    add_conf CONFIG_ENV_DEFAULT_ENV_TEXT_FILE "\"board/\$(VENDOR)/\$(BOARD)/${UBOOT_ENV_FILE}.env\""

    if [ -n "${UBOOT_CONFIG}" -o -n "${UBOOT_DELTA_CONFIG}" ]
    then
        unset i j
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    c="${B}/${config}-${type}"
                    oe_runmake -C "${c}" oldconfig
                    merge_config.sh -m -r -O "${c}" "${c}/.config" "$tmpfile"
                    oe_runmake -C "${c}" oldconfig
                fi
            done
            unset j
        done
        unset i
    else
        oe_runmake -C "${B}" oldconfig
        merge_config.sh -m -r -O "${B}" "${B}/.config" "$tmpfile"
        oe_runmake -C "${B}" oldconfig
    fi
    rm -f "$tmpfile"

    if ${@ bb.utils.contains('DISTRO_FEATURES', 'u-boot-fw-utils', "true", "false", d)};then
        env_offset=$(sed -n '/^CONFIG_ENV_OFFSET=/{s/^.*=//;p}' ${c}/.config)
        env_size=$(sed -n '/^CONFIG_ENV_SIZE=/{s/^.*=//;p}' ${c}/.config)
        printf "/dev/mmcblk0boot0\t0x%08x\t0x%08x\n" "$env_offset" "$env_size" > ${WORKDIR}/fw_env.config
    fi
}
addtask adjust_config before do_compile after do_configure

# deploy only; a file in UNPACKDIR would also land in the rootfs
do_deploy:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'u-boot-fw-utils', 'true', 'false', d)}; then
        install -D -m 0644 "${WORKDIR}/fw_env.config" "${DEPLOYDIR}/u-boot/fw_env.config"
    fi
}

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
