SUMMARY = "Linux Kernel for Ka-Ro electronics Computer-On-Modules"

require recipes-kernel/linux/linux-karo.inc

DEPENDS += "lzop-native bc-native dtc-native"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRCBRANCH = "lf-5.15.y-karo"
SRCREV = "1bc4508ac322388f231561cd06b6d49281ac1b1c"
KERNEL_SRC = "git://github.com/karo-electronics/karo-tx-linux.git"
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-5.15/patches:${THISDIR}/${PN}-5.15:"

PROVIDES += "linux"

SRC_URI = "${KERNEL_SRC};protocol=https;branch=${SRCBRANCH}"

SRC_URI:append = "${@ "".join(map(lambda f: " file://cfg/" + f, "${KERNEL_FEATURES}".split()))}"

# automatically add all .dts files referenced by ${KERNEL_DEVICETREE} to SRC_URI
SRC_URI:append:mx8-nxp-bsp = "${@"".join(map(lambda f: " file://dts/%s;subdir=git/${KERNEL_OUTPUT_DIR}" % f.replace(".dtb", ".dts"), "${KERNEL_DEVICETREE}".split()))}"

SRC_URI:append:mx8-nxp-bsp = " \
	file://dts/freescale/imx8m-qs8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8m-tx8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mm-tx8m.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
	file://dts/freescale/imx8mp-karo.dtsi;subdir=git/${KERNEL_OUTPUT_DIR} \
"

SRC_URI:append:mx8-nxp-bsp = " \
	file://0003-vf610-gpio.patch \
"

KARO_BOARD_PMIC ??= ""

SRC_URI:append:mx8mm-nxp-bsp = " \
	file://mx8mm_defconfig \
"
SRC_URI:append:mx8mn-nxp-bsp = " \
	file://mx8mn_defconfig \
"
SRC_URI:append:mx8mp-nxp-bsp = " \
	file://mx8mp_defconfig \
"

SRC_URI:append:qs8m = " \
	file://0001-mx6s-capture-add-rggb8-video-format.patch \
	file://0002-imx219-driver-zeus-version.patch \
"

KERNEL_LOCALVERSION = "${LINUX_VERSION_EXTENSION}"
KERNEL_IMAGETYPE = "Image"

KBUILD_DEFCONFIG:mx8mm-nxp-bsp = "mx8mm_defconfig"
KBUILD_DEFCONFIG:mx8mn-nxp-bsp = "mx8mn_defconfig"
KBUILD_DEFCONFIG:mx8mp-nxp-bsp = "mx8mp_defconfig"

KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','basler',' basler.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','bluetooth',' bluetooth.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','csi-camera',' csi-camera.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','imx219',' imx219.cfg mx8-cam.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','ipv6',' ipv6.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','dsi83',' dsi83.cfg lvds.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','tc358867',' tc358867.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','lvds',' lvds.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','raspi-display',' raspi-display.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','systemd',' systemd.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','wifi',' wifi.cfg','',d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','pcie',' pcie.cfg apex.cfg','',d)}"
KERNEL_FEATURES:append = "${@' ${KARO_BOARD_PMIC}.cfg' if d.getVar('KARO_BOARD_PMIC') != '' else ''}"
KERNEL_FEATURES:append = "${@bb.utils.contains('DISTRO_FEATURES','flexcan',' flexcan.cfg','',d)}"

KERNEL_FEATURES:append:tx8m-1620 = " no-suspend.cfg"
KERNEL_FEATURES:append:tx8m-1622 = " no-suspend.cfg"

COMPATIBLE_MACHINE = "(mx8-nxp-bsp)"

EXTRA_OEMAKE:append = " V=0"
KERNEL_DTC_FLAGS += "-@"

do_check_config() {
    applied=$(fgrep -f "${WORKDIR}/${KBUILD_DEFCONFIG}" ${B}/.config | wc -l)
    configured=$(cat "${WORKDIR}/${KBUILD_DEFCONFIG}" | wc -l)
    if [ $applied != $configured ];then
        bbwarn "The following items of ${KBUILD_DEFCONFIG} have not been accepted by Kconfig"
        bbwarn "$(fgrep -f "${WORKDIR}/${KBUILD_DEFCONFIG}" ${B}/.config | \
                fgrep -vf - "${WORKDIR}/${KBUILD_DEFCONFIG}")"
    fi
    applied="$(fgrep -f "${WORKDIR}/${KBUILD_DEFCONFIG}" "${B}/defconfig" | wc -l)"
    if [ $applied != $configured ];then
        p="$(fgrep -f "${WORKDIR}/${KBUILD_DEFCONFIG}" "${B}/defconfig" | \
                fgrep -vf - "${WORKDIR}/${KBUILD_DEFCONFIG}" | \
                    sed 's/^# //;s/[= ].*$//')"
        for pat in $p;do
            bbwarn "'$(fgrep -w "$pat" "${WORKDIR}/${KBUILD_DEFCONFIG}")' is obsolete in '${KBUILD_DEFCONFIG}')'"
        done
    fi

    for f in ${KERNEL_FEATURES};do
        applied=$(fgrep -f ${WORKDIR}/cfg/$f ${B}/.config | wc -l)
        configured=$(cat ${WORKDIR}/cfg/$f | wc -l)
        if [ $applied != $configured ];then
            bbwarn "The following items of config fragment $f have not been accepted by Kconfig:"
            bbwarn "$(fgrep -f ${WORKDIR}/cfg/$f ${B}/.config | fgrep -vf - ${WORKDIR}/cfg/${f})"
        fi
        applied="$(fgrep -f "${WORKDIR}/cfg/${f}" "${B}/defconfig" | wc -l)"
        if [ $applied != $configured ];then
            p="$(fgrep -f "${WORKDIR}/cfg/${f}" "${B}/defconfig" | \
                    fgrep -vf - "${WORKDIR}/cfg/${f}" | \
                    sed 's/^# //;s/[= ].*$//')"
            for pat in $p;do
                bbwarn "'$(fgrep -w "$pat" "${WORKDIR}/cfg/${f}")' is obsolete in '$f')'"
            done
        fi
    done
}
addtask do_check_config after do_savedefconfig
do_check_config[nostamp] = "1"

do_configure:prepend() {
    # Add GIT revision to the local version
    head=`git --git-dir=${S}/.git rev-parse --verify --short HEAD 2> /dev/null`
    if ! [ -s "${S}/.scmversion" ] || ! grep -q "$head" ${S}/.scmversion;then
        echo "+g$head" > "${S}/.scmversion"
    fi
    install -v "${WORKDIR}/${KBUILD_DEFCONFIG}" "${B}/.config"
    sed -i '/CONFIG_LOCALVERSION/d' "${B}/.config"
    echo 'CONFIG_LOCALVERSION="${KERNEL_LOCALVERSION}"' >> "${B}/.config"

    for f in ${KERNEL_FEATURES};do
         ${S}/scripts/kconfig/merge_config.sh -O ${B} -m ${B}/.config ${WORKDIR}/cfg/$f
    done
}
addtask do_configure before do_devshell

do_compile_dtbs() {
    oe_runmake -C ${B} DTC_FLAGS="${KERNEL_DTC_FLAGS}" ${KERNEL_DEVICETREE}
}
addtask do_compile_dtbs after do_configure before do_check_dtbs

python do_check_dtbs () {
    import os

    def get_ovname(name):
        pfx = d.getVar('SOC_PREFIX')
        fam = d.getVar('SOC_FAMILY')
        fn = []
        for n in name.split(","):
            if os.path.exists("%s-%s.dtb" % (fam, n)):
                fn.append("%s-%s.dtb" % (fam, n))
            elif os.path.exists("%s-%s.dtb" % (pfx, n)):
                fn.append("%s-%s.dtb" % (pfx, n))
            else:
                bb.fatal("Overlay file '[%s,%s]-%s.dtb' not found" % (fam, pfx, n))
        return fn

    def apply_overlays(infile, outfile, overlays):
        pfx = d.getVar('SOC_PREFIX')
        ovlist = []
        for f in overlays.split():
            ovlist += get_ovname(f)
        if len(ovlist) == 0:
            bb.fatal("No files found for overlays %s" % overlays)
            return
        bb.debug(2, "ovlist=%s" % " ".join(ovlist))
        ovfiles = " ".join(map(lambda f: "'%s'" % f, ovlist))
        bb.debug(2, "ovfiles=%s" % ovfiles)
        cmd = ("fdtoverlay -i '%s.dtb' -o '%s.dtb' %s" % (infile, outfile, ovfiles))
        bb.debug(2, "%s" % cmd)
        os.system("pwd")
        os.system("ls -lH *.dtb | grep -v `date +%Y%m%d`")
        if os.system("%s" % cmd):
            bb.fatal("Failed to apply overlays %s for baseboard '%s' to '%s.dtb'" %
                     (",".join(ovfiles.split()), baseboard, infile))
            return
        bb.note("FDT overlays %s for '%s' successfully applied to '%s.dtb'" %
            (ovfiles, baseboard, infile))

    here = os.getcwd()
    baseboards = d.getVar('KARO_BASEBOARDS')
    if baseboards == None:
        bb.warn("KARO_BASEBOARDS is not set; cannot process FDT overlays")
        return
    basename = d.getVar('DTB_BASENAME')
    if basename == None:
        bb.warn("DTB_BASENAME is not set; cannot process FDT overlays")
        return
    os.chdir(os.path.join(d.getVar('B'), d.getVar('KERNEL_OUTPUT_DIR'), "dts", "freescale"))
    for baseboard in baseboards.split():
        bb.debug(2, "creating %s-%s.dtb from %s.dtb" % (basename, baseboard, basename))
        outfile = "%s-%s" % (basename, baseboard)
        overlays = " ".join(map(lambda f: f, d.getVarFlag('KARO_DTB_OVERLAYS', baseboard, True).split()))
        bb.note("overlays_%s=%s" % (baseboard, overlays))
        if overlays == None or len(overlays.split()) == 0:
            bb.fatal("%s: No overlays specified for %s" % (d.getVar('MACHINE'), baseboard))
        bb.debug(2, "overlays for %s-%s='%s'" %
                 (basename, baseboard, "','".join(overlays.split())))
        apply_overlays(basename, outfile, overlays)
    os.chdir(here)
}
addtask do_check_dtbs after do_compile_dtbs before do_compile
