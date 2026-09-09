FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://patches/0001-board-add-support-for-qs95-5610-module-and-qsbase95-.patch \
    file://patches/0002-env-implement-karo-environment.patch \
    file://patches/0003-dts-move-u-boot-include-to-base-dts.patch \
"

# Create symlink to fix path resolution error when using devtool
do_create_fix_symlink() {
    if [ -n "${UBOOT_CONFIG}" -o -n "${UBOOT_DELTA_CONFIG}" ]
    then
        mkdir -p ${B}

        unset i j
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    ln -sf ${B}/${config}-${type} ${B}/${config}
                    return
                fi
            done
            unset j
        done
        unset i
    fi
}
addtask create_fix_symlink before do_configure after do_unpack

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
    add_conf CONFIG_DEFAULT_FDT_FILE "\"${UBOOT_DTB_NAME}.dtb\""
    add_conf CONFIG_ENV_SOURCE_FILE "\"${UBOOT_ENV_FILE}\""

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
}
addtask adjust_config before do_compile after do_configure
