FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:${THISDIR}/${PN}/patches:${THISDIR}/${PN}/cfg:"

require u-boot-karo-configure.inc

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
    file://0009-dts-qs95-write-the-KSZ9131-pad-skews-in-the-KSZ9131-.patch \
    file://0010-dts-qs95-drive-PCIE_PWR_EN-from-the-M.2-regulator.patch \
    file://0011-dts-qs95-256-MiB-CMA-for-a-2-GiB-module.patch \
    file://0012-dts-qs95-no-eDMA1-on-LPUART1-ERR052900.patch \
    file://0013-dts-qs95-disable-USB-park-mode-in-HS-ERR052487.patch \
    file://0014-board-karo-qs95-cap-PCIe-read-requests-ERR051382.patch \
    file://0015-i2c-imx_lpi2c-repeated-start-between-read-chunks-ERR.patch \
    file://0016-configs-qs95-persist-the-DDR-quickboot-data-in-SPL.patch \
"

UBOOT_FEATURES:append:qs95 = " fastboot"

SRC_URI:append = "${@ "".join(" file://%s.cfg" % f for f in (d.getVar('UBOOT_FEATURES') or "").split())}"

UBOOT_BOARD_DIR:qs95 = "board/karo/imx95"

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
