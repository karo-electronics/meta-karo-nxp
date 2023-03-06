FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}:${THISDIR}/${BP}/patches:${THISDIR}/${BP}/env:"
SRC_URI:append = " \
	file://karo.bmp;subdir=git/tools/logos \
"

UBOOT_ENV_FILE ?= "${@ "${MACHINE}%s_env.txt" % ("-${KARO_BASEBOARD}" if "${KARO_BASEBOARD}" != "" else "")}"

SRC_URI:append = " \
	${@ "file://${UBOOT_ENV_FILE};subdir=git/board/karo/tx8m" if "${UBOOT_ENV_FILE}" != "" else ""} \
	file://dts/imx8m-tx8m-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-karo.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-qs8m-mq00-qsbase2-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-qs8m-mq00-qsbase2.dts;subdir=git/arch/arm \
	file://dts/imx8mm-qs8m-mq00-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-qs8m-mq00.dts;subdir=git/arch/arm \
	file://dts/imx8mm-qsxm-mm60-qsbase3-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-qsxm-mm60-qsbase3.dts;subdir=git/arch/arm \
	file://dts/imx8mm-qsxm-mm60-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-qsxm-mm60.dts;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1610-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1610.dts;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1620-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1620.dts;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1622-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-1622.dts;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-16xx.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-tx8m-mipi-mb.dtsi;subdir=git/arch/arm \
	file://dts/imx8mm-tx8mm-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mn-karo.dtsi;subdir=git/arch/arm \
	file://dts/imx8mn-qs8m-nd00-qsbase2-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mn-qs8m-nd00-qsbase2.dts;subdir=git/arch/arm \
	file://dts/imx8mn-qs8m-nd00-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mn-qs8m-nd00.dts;subdir=git/arch/arm \
	file://dts/imx8mn-tx8m-nd00-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mn-tx8m-nd00.dts;subdir=git/arch/arm \
	file://dts/imx8mn-tx8mn-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-karo.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-qsxp-ml81-qsbase3-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-qsxp-ml81-qsbase3.dts;subdir=git/arch/arm \
	file://dts/imx8mp-qsxp-ml81-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-qsxp-ml81.dts;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-lvds-mb.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml81-lvds-mb-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml81-lvds-mb.dts;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml81-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml81.dts;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml82-lvds-mb-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml82-lvds-mb.dts;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml82-u-boot.dtsi;subdir=git/arch/arm \
	file://dts/imx8mp-tx8p-ml82.dts;subdir=git/arch/arm \
"

require conf/machine/include/${SOC_PREFIX}-overlays.inc

UBOOT_BOARD_DIR = "board/karo/tx8m"
UBOOT_FEATURES:append = "${@ bb.utils.contains('DISTRO_FEATURES', "copro", " copro", "", d)}"

python do_env_overlays () {
    import os
    import shutil

    if d.getVar('KARO_BASEBOARDS') == None:
        bb.warn("KARO_BASEBOARDS is undefined")
        return 1

    def update_env_file (src_file, dst_dir):
        bb.utils.mkdirhier(dst_dir)
        env_file = os.path.join(dst_dir, os.path.basename(src_file))
        shutil.copyfile(src_file, env_file)
        f = open(env_file, 'a')
        for baseboard in d.getVar('KARO_BASEBOARDS').split():
            ovlist = d.getVarFlag('KARO_DTB_OVERLAYS', baseboard, True)
            if ovlist == None:
                bb.note("No overlays defined for '%s' on baseboard '%s'" % (d.getVar('MACHINE'), baseboard))
                continue
            overlays = " ".join(map(lambda f: f, ovlist.split()))
            bb.note("Adding overlays_%s='%s' to %s" % (baseboard, overlays, env_file))
            f.write("overlays_%s=%s\n" %(baseboard, overlays))
        f.write("soc_prefix=%s\n" % (d.getVar('SOC_PREFIX') or ""))
        f.write("soc_family=%s\n" % (d.getVar('SOC_FAMILY') or ""))
        f.close()

    src_file = os.path.join(d.getVar('S'), d.getVar('UBOOT_BOARD_DIR'), d.getVar('UBOOT_ENV_FILE'))
    if d.getVar('UBOOT_CONFIG') != None:
        for config in d.getVar('UBOOT_MACHINE').split():
            dst_dir = os.path.join(d.getVar('B'), config, d.getVar('UBOOT_BOARD_DIR'))
            update_env_file(src_file, dst_dir)
    else:
        dst_dir = os.path.join(d.getVar('B'), d.getVar('UBOOT_BOARD_DIR'))
        update_env_file(src_file, dst_dir)
}
addtask do_env_overlays before do_compile after do_configure
