# Override of the u-boot-configure.inc hook: assemble the config in
# ${B}/${config} from the Ka-Ro config fragments instead of using a
# defconfig from ${S}/configs
uboot_configure_config() {
    config=$1
    type=$2

    bbnote "Copying 'u-boot-cfg.${SOC_PREFIX}' to '${B}/${config}/.config'"
    mkdir -p "${B}/${config}"
    cat "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" > "${B}/${config}/.config"
    if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
        bbnote "Appending 'u-boot-cfg.${SOC_FAMILY}' to '${B}/${config}/.config'"
        cat "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" >> "${B}/${config}/.config"
    fi
    bbnote "Appending 'u-boot-cfg.${MACHINE}' to '${B}/${config}/.config'"
    cat "${WORKDIR}/u-boot-cfg.${MACHINE}" >> "${B}/${config}/.config"
    if [ -s "${WORKDIR}/u-boot-cfg.${type}" ];then
        bbnote "Appending 'u-boot-cfg.${type}' to '${B}/${config}/.config'"
        cat "${WORKDIR}/u-boot-cfg.${type}" >> "${B}/${config}/.config"
    fi
    oe_runmake -C ${S} O=${B}/${config} olddefconfig
    if [ -n "${@' '.join(find_cfgs(d))}" ]; then
        merge_config.sh -m -r -O ${B}/${config} ${B}/${config}/.config ${@" ".join(find_cfgs(d))}
        oe_runmake -C ${S} O=${B}/${config} oldconfig
    fi
}

# Override of the u-boot-configure.inc hook for single-config builds
uboot_configure() {
    bbnote "Copying 'u-boot-cfg.${SOC_PREFIX}' to '${B}/.config'"
    mkdir -p "${B}"
    cat "${WORKDIR}/u-boot-cfg.${SOC_PREFIX}" > "${B}/.config"
    if [ "${SOC_FAMILY}" != "${SOC_PREFIX}" ];then
        bbnote "Appending 'u-boot-cfg.${SOC_FAMILY}' to '${B}/.config'"
        cat "${WORKDIR}/u-boot-cfg.${SOC_FAMILY}" >> "${B}/.config"
    fi
    bbnote "Appending 'u-boot-cfg.${MACHINE}' to '${B}/.config'"
    cat "${WORKDIR}/u-boot-cfg.${MACHINE}" >> "${B}/.config"
    oe_runmake -C ${S} O=${B} olddefconfig
}

# Apply Ka-Ro specific settings (default DTB and default env file).
do_configure:append() {
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

    bbnote "UBOOT_ENV_FILE='${UBOOT_ENV_FILE}'"
    if [ -n "${UBOOT_ENV_FILE}" ];then
        add_conf CONFIG_USE_DEFAULT_ENV_FILE y
        add_conf CONFIG_DEFAULT_ENV_FILE "\"board/\$(VENDOR)/\$(BOARD)/${UBOOT_ENV_FILE}.env\""
        if  ${@ bb.utils.contains('DISTRO_FEATURES', 'rauc', "true", "false", d)};then
            LC_ALL=C sort ${WORKDIR}/${UBOOT_ENV_FILE}.env ${WORKDIR}/rauc.env > ${S}/${UBOOT_BOARD_DIR}/${UBOOT_ENV_FILE}.env
        else
            LC_ALL=C sort ${WORKDIR}/${UBOOT_ENV_FILE}.env > ${S}/${UBOOT_BOARD_DIR}/${UBOOT_ENV_FILE}.env
        fi
    else
        echo "# CONFIG_USE_DEFAULT_ENV_FILE is not set" >> "$tmpfile"
    fi

    # convey common settings for imx-atf and U-Boot to U-Boot config
    add_conf CONFIG_BL31_BASE              ${ATF_BL31_BASE}
    add_conf CONFIG_BL31_SIZE              ${ATF_BL31_SIZE}
    add_conf CONFIG_SAVED_DRAM_TIMING_BASE ${UBOOT_SAVED_DRAM_TIMING_BASE}

    if [ -n "${UBOOT_CONFIG}" ];then
        for config in ${UBOOT_MACHINE};do
            c="${B}/${config}"
            oe_runmake -C "${c}" oldconfig
            merge_config.sh -m -r -O "${c}" "${c}/.config" "$tmpfile"
            oe_runmake -C "${c}" oldconfig
        done
    else
        c=${B}
        oe_runmake -C "${B}" oldconfig
        merge_config.sh -m -r -O "${B}" "${B}/.config" "$tmpfile"
        oe_runmake -C "${B}" oldconfig
    fi

    rm -f "$tmpfile"

    if ${@ bb.utils.contains('DISTRO_FEATURES', 'u-boot-fw-utils', "true", "false", d)};then
        env_offset=$(sed -n '/^CONFIG_ENV_OFFSET=/{s/^.*=//;p}' ${c}/.config)
        env_size=$(sed -n '/^CONFIG_ENV_SIZE=/{s/^.*=//;p}' ${c}/.config)
        if ${@ bb.utils.contains('MACHINE_FEATURES', 'nand', "true", "false", d)};then
            # CONFIG_ENV_OFFSET is relative to the whole NAND chip, whereas
            # KARO_FW_ENV_NAND is the env partition on Linux, where the
            # environment sits at offset 0. CONFIG_ENV_RANGE spans the
            # bad-block area, given as a sector count.
            env_range=$(sed -n '/^CONFIG_ENV_RANGE=/{s/^.*=//;p}' ${c}/.config)
            if [ -z "$env_range" ];then
                env_range=$env_size
            fi
            sectors=$(expr "$(printf '%d' "$env_range")" / "$(printf '%d' "$env_size")")
            printf "%s\t0x%08x\t0x%08x\t0x%08x\t%d\n" \
                "${KARO_FW_ENV_NAND}" 0 "$env_size" "$env_size" "$sectors" > ${WORKDIR}/fw_env.config
        else
            printf "%s\t0x%08x\t0x%08x\n" \
                "${KARO_FW_ENV_MMC}" "$env_offset" "$env_size" > ${WORKDIR}/fw_env.config
        fi
    fi
}

do_configure:append:mx6-nxp-bsp() {
    tmpfile="`mktemp cfg-XXXXXX.tmp`"
    if [ -z "$tmpfile" ];then
        bbfatal "Failed to create tmpfile"
    fi
    add_conf() {
        [ $# = 2 ] || return 0
        echo "$1=$2" >> "$tmpfile"
    }

    add_conf CONFIG_KARO_DTB_SELECT         y
    add_conf CONFIG_DEFAULT_DEVICE_TREE     "\"${KARO_UBOOT_DTB_BASENAME}\""
    add_conf CONFIG_OF_LIST                 "\"${KARO_UBOOT_OF_LIST}\""
    add_conf CONFIG_KARO_DEFAULT_BASEBOARD  "\"${KARO_BASEBOARD}\""
    
    if [ -n "${UBOOT_CONFIG}" ];then
        for config in ${UBOOT_MACHINE};do
            c="${B}/${config}"
            merge_config.sh -m -r -O "$c" "$c/.config" "$tmpfile"
            oe_runmake -C "$c" oldconfig
            grep -q "^CONFIG_KARO_DTB_SELECT=y" "$c/.config" || \
                bbfatal "kconfig rejected CONFIG_KARO_DTB_SELECT in '$(basename "$c")'"
        done
    else
        merge_config.sh -m -r -O "${B}" "${B}/.config" "$tmpfile"
        oe_runmake -C "${B}" oldconfig
        grep -q "^CONFIG_KARO_DTB_SELECT=y" "${B}/.config" || \
            bbfatal "kconfig rejected CONFIG_KARO_DTB_SELECT in '$(basename "${B}")'"
    fi

    rm -f "$tmpfile"
}
