FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:${THISDIR}/${PN}-${PV}/patches:${THISDIR}/${PN}-${PV}/env:"
SRC_URI:append = " \
	file://karo.bmp;subdir=git/tools/logos \
"

UBOOT_ENV_FILE ?= "${@ "%s%s_env.txt" % (d.getVar('MACHINE'), "-" + d.getVar('KARO_BASEBOARD')) if d.getVar('KARO_BASEBOARD') != "" else ""}"

SRC_URI:append = " \
	${@ "file://%s;subdir=git/board/karo/tx8m/" % d.getVar('UBOOT_ENV_FILE') if d.getVar('UBOOT_ENV_FILE') != "" else ""} \
	file://qsbase4-dts-files.patch \
	file://dts/imx8mm-qs8m-mq00-qsbase4.dts;subdir=git/arch/arm \
	file://dts/imx8mm-qs8m-mq00-qsbase4-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-qsxm-mm60-qsbase4.dts;subdir=git/arch/arm \
	file://dts/imx8mm-qsxm-mm60-qsbase4-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mn-qs8m-nd00-qsbase4.dts;subdir=git/arch/arm \
	file://dts/imx8mn-qs8m-nd00-qsbase4-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-qsxp-ml81-qsbase4.dts;subdir=git/arch/arm \
	file://dts/imx8mp-qsxp-ml81-qsbase4-u-boot.dtsi;subdir=git/arch/arm \
"

do_configure:append:mx8m-nxp-bsp () {
    if [ -n "${UBOOT_CONFIG}" ];then
        for config in ${UBOOT_MACHINE};do
            c="${B}/${config}"
            if [ "${KARO_BASEBOARD}" != "" ];then
                cat <<EOF >> "${c}/.config"
CONFIG_DEFAULT_DEVICE_TREE="${DTB_BASENAME}-${KARO_BASEBOARD}"
CONFIG_DEFAULT_ENV_FILE="board/\$(VENDOR)/\$(BOARD)/${UBOOT_ENV_FILE}"
EOF
            fi
        done
    fi
}
addtask do_configure before do_devshell
