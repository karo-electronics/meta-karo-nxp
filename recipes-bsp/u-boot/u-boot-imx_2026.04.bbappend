FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-board-add-support-for-qs95-5610-module-and-qsbase95-.patch file://0002-configs-add-qs95-5610-defconfig-file.patch file://0003-dts-add-dts-files-for-qs95-5610-module-and-qsbase95-.patch"

# Create symlink to fix path resolution error when using devtool
do_create_symlink_fix() {
    if [ -n "${UBOOT_CONFIG}" -o -n "${UBOOT_DELTA_CONFIG}" ]
    mkdir -p ${B}
    then
        unset i j
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    ln -sf ${B}/${config}-${type} ${B}/${config}
                fi
            done
            unset j
        done
        unset i
    fi
}
addtask create_symlink_fix before do_configure after do_unpack
