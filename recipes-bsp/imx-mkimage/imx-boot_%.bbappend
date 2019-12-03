FILESEXTRAPATHS_prepend := "${THISDIR}/mkimage:"
SRC_URI_append = " \
		file://0001-iMX8M-soc.mak-fix-missing-dependencies.patch \
		file://0002-iMX8M-soc.mak-add-support-for-TX8M.patch \
"

do_compile_prepend() {
    export dtbs_ddr3l=${UBOOT_DTB_NAME}
}
