FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:${THISDIR}/${PN}/patches:${THISDIR}/${PN}/cfg:"

require u-boot-karo-configure.inc

inherit fsl-u-boot-localversion
LOCALVERSION = "-karo"

SRC_URI += "file://0001-arm-mach-imx-imx9-make-imx_get_mac_from_fuse-weak.patch"
SRC_URI += "file://0002-i2c-imx_lpi2c-work-around-ERR053261-on-i.MX95.patch"
SRC_URI += "file://0003-mmc-fsl_esdhc_imx-fail-HS400-setup-when-the-strobe-D.patch"
SRC_URI += "file://0004-fastboot-do-not-tie-UUU-support-to-the-FSL-implement.patch"
SRC_URI += "file://0005-board-karo-add-common-board-support-code.patch"
SRC_URI += "file://0006-board-karo-add-i.MX95-module-family-support.patch"
SRC_URI += "file://0007-board-karo-imx95-add-support-for-the-QS95-9610-modul.patch"

UBOOT_FEATURES:append = "${@ bb.utils.contains('MACHINE_FEATURES', "quickboot", " quickboot", "", d)}"
UBOOT_FEATURES:append = "${@ bb.utils.contains('MACHINE_FEATURES', "optee", " optee", "", d)}"
UBOOT_FEATURES:append = "${@ bb.utils.contains('DISTRO_FEATURES', "copro", " copro", "", d)}"
UBOOT_FEATURES:append = " fastboot"

SRC_URI:append = "${@ "".join(" file://%s.cfg" % f for f in (d.getVar('UBOOT_FEATURES') or "").split())}"

UBOOT_BOARD_DIR:qs95 = "board/karo/imx95"

# also deploy a plain fw_env.config next to the imx-boot image for
# flashing tools; the rootfs copy is installed via ${UNPACKDIR}/fw_env.config
do_deploy:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'u-boot-fw-utils', 'true', 'false', d)}; then
        install -D -m 0644 "${UNPACKDIR}/fw_env.config" "${DEPLOYDIR}/u-boot/fw_env.config"
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
