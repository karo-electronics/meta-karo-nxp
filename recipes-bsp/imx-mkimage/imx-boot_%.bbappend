SRCBRANCH = "imx_4.14.98_2.3.0"
SRCREV = "2556000499f667123094af22326cfd8e4cbadaac"

FILESEXTRAPATHS_prepend := "${THISDIR}/mkimage:"
SRC_URI_append = " \
		file://0001-iMX8M-soc.mak-fix-missing-dependencies.patch \
		file://0002-iMX8M-soc.mak-add-support-for-TX8M.patch \
"

do_compile_prepend() {
    export dtbs_ddr3l=${UBOOT_DTB_NAME}
}
