FILESEXTRAPATHS_prepend := "${THISDIR}/mkimage:"
SRC_URI_append = " \
		file://imx8qxb0-bugfixes.patch \
		file://mkimage-fit-atf-bugfixes.patch \
		file://make-clean-bugfix.patch \
		file://make-dependencies.patch \
		file://tx8m-support.patch \
		file://cleanup.patch \
		file://no-tee.patch \
"

do_compile_prepend() {
    export dtbs=${UBOOT_DTB_NAME}
    export dtbs_ddr3l=${UBOOT_DTB_NAME}
}
