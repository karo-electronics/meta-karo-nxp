
# Create symlink to fix path resolution error when using devtool
do_configure:prepend() {
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
