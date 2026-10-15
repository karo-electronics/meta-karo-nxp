# Copyright (C) 2012, 2015 O.S. Systems Software LTDA.
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "GPL-2.0-only"

inherit kernel fsl-vivante-kernel-driver-handler
inherit fsl-kernel-localversion
LOCALVERSION = "-karo"

PROVIDES += "linux"
DEPENDS += "lzop-native bc-native dtc-native"

S = "${WORKDIR}/git"

KARO_KERNEL_SRC ?= "${KERNEL_SRC_DEFAULT}"
KARO_KERNEL_BRANCH ?= "${KERNEL_BRANCH_DEFAULT}"
KARO_KERNEL_REV ?= "${KERNEL_REV_DEFAULT}"

KERNEL_SRC = "${KARO_KERNEL_SRC}"
KERNEL_BRANCH = "${KARO_KERNEL_BRANCH}"
KERNEL_REV = "${KARO_KERNEL_REV}"

SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"
SRCBRANCH = "${KERNEL_BRANCH}"
SRCREV = "${KERNEL_REV}"

# automatically add all .dts files referenced by ${KERNEL_DEVICETREE} to SRC_URI
SRC_URI:append = "${@"".join(map(lambda f: " file://dts/%s;subdir=git/${KERNEL_OUTPUT_DIR}" % f.replace(".dtb", ".dts"), "${KERNEL_DEVICETREE}".split()))}"

SRC_URI:append = "${@ "".join(map(lambda f: " file://dts/freescale/includes/%s-%s.dtsi;subdir=git/${KERNEL_OUTPUT_DIR}" % (d.getVar('SOC_PREFIX'), f), d.getVar('DTB_OVERLAY_INCLUDES').split()))}"

SRC_URI:append = " file://${KBUILD_DEFCONFIG}"

SRC_URI:append = "${@ "".join(map(lambda f: " file://" + f, "${KERNEL_FEATURES}".split()))}"

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
        applied=$(fgrep -f ${WORKDIR}/$f ${B}/.config | wc -l)
        configured=$(cat ${WORKDIR}/$f | wc -l)
        if [ $applied != $configured ];then
            bbwarn "The following items of config fragment $f have not been accepted by Kconfig:"
            bbwarn "$(fgrep -f ${WORKDIR}/$f ${B}/.config | fgrep -vf - ${WORKDIR}/${f})"
        fi
        applied="$(fgrep -f "${WORKDIR}/${f}" "${B}/defconfig" | wc -l)"
        if [ $applied != $configured ];then
            p="$(fgrep -f "${WORKDIR}/${f}" "${B}/defconfig" | \
                    fgrep -vf - "${WORKDIR}/${f}" | \
                    sed 's/^# //;s/[= ].*$//')"
            for pat in $p;do
                bbwarn "'$(fgrep -w "$pat" "${WORKDIR}/${f}")' is obsolete in '$f')'"
            done
        fi
    done
}
addtask do_check_config after do_savedefconfig
do_check_config[nostamp] = "1"

do_configure:prepend() {
    install -v "${WORKDIR}/${KBUILD_DEFCONFIG}" "${B}/.config"
    
    for f in ${KERNEL_FEATURES};do
         ${S}/scripts/kconfig/merge_config.sh -O ${B} -m ${B}/.config ${WORKDIR}/$f
    done
}
addtask do_configure before do_devshell

do_configure:append() {
    oe_runmake -C ${B} oldconfig
}

do_compile_dtbs() {
    oe_runmake -C ${B} DTC_FLAGS="${KERNEL_DTC_FLAGS}" ${KERNEL_DEVICETREE}
}
addtask do_compile_dtbs after do_configure before do_check_dtbs

python do_check_dtbs () {
    import os
    gov = d.getVar('DTB_OVERLAYS_generic').split()
    ov = d.getVar('DTB_OVERLAYS').split()

    def get_ovname(name):
        pfx = d.getVar('SOC_PREFIX')
        fam = d.getVar('SOC_FAMILY')
        fn = []
        for n in name.split(","):
            ovn = ""
            if n in gov:
                ovn = "%s-%s.dtbo" % (pfx, n)
            elif n in ov:
                ovn = "%s-%s.dtbo" % (fam, n)
            else:
                continue
            if os.path.exists(ovn):
                fn.append(ovn)
            else:
                bb.fatal("Overlay file '%s' not found" % ovn)
        return fn

    def apply_overlays(infile, outfile, overlays):
        pfx = d.getVar('SOC_PREFIX')
        ovlist = []
        for f in overlays.split():
            ovlist += get_ovname(f)
        if len(ovlist) == 0:
            bb.fatal("No files found for overlays %s" % overlays)
            return
        ovfiles = " ".join(map(lambda f: "'%s'" % f, ovlist))
        cmd = ("fdtoverlay -i '%s.dtb' -o '%s.dtb' %s" % (infile, outfile, ovfiles))
        bb.debug(2, "%s" % cmd)
        os.system("pwd")
        os.system("ls -lH *.dtb{,o} | grep -v `date +%Y%m%d`")
        if os.system("%s" % cmd):
            bb.fatal("Failed to apply overlays %s for baseboard '%s' to '%s.dtb'" %
                     (" ".join(ovfiles.split()), baseboard, infile))
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
        ovlist = d.getVarFlag('KARO_DTB_OVERLAYS', baseboard, True)
        if ovlist == None or len(ovlist.split()) == 0:
            bb.warn("%s: No overlays specified for %s" % (d.getVar('MACHINE'), baseboard))
            continue
        overlays = " ".join(map(lambda f: f, ovlist.split()))
        bb.note("overlays_%s=%s" % (baseboard, overlays))
        bb.debug(2, "overlays for %s-%s='%s'" %
                 (basename, baseboard, "','".join(overlays.split())))
        apply_overlays(basename, outfile, overlays)
    os.chdir(here)
}
addtask do_check_dtbs after do_compile_dtbs before do_compile

python do_check_overlays() {
    import itertools
    import os
    import re
    import subprocess

    kb = d.getVar('KARO_BASEBOARDS').split()
    kbb = d.getVarFlags('KARO_DTB_OVERLAYS')
    bb.debug(1, "kbb='%s'" % kbb)
    all = []
    for b in kbb:
        if b not in kb:
            bb.note("baseboard '%s' is not listed in KARO_BASEBOARDS: [%s]" % (b, " ".join(kb)))
        ovl = d.getVarFlag('KARO_DTB_OVERLAYS', b, True)
        if ovl is None:
            continue

        for ov in re.split(r'[ ,]+', ovl.strip()):
                bb.debug(1, "ov='%s'" % ov)
                if ov not in all:
                    all += [ov]
                    bb.note("all=[%s]" % " ".join(all))

    ovlist = d.getVar('DTB_OVERLAYS').split()
    ovlist += d.getVar('DTB_OVERLAYS_generic').split()
    for baseboard in kb:
        bb.debug(2, "Checking baseboard '%s'" % baseboard);
        ovb = d.getVarFlag('KARO_DTB_OVERLAYS', baseboard, True)
        if ovb is None:
            bb.warn("No overlays defined for '%s' on baseboard '%s'" % (d.getVar('MACHINE'), baseboard))
            continue
        bb.note("KARO_DTB_OVERLAYS[%s]=%s" % (baseboard, ",".join(ovb.split())))
        for ov in re.split(r'[ ,]+', ovb.strip()):
            bb.debug(2, "ov='%s'" % ov)
            if ov not in ovlist:
                bb.warn("'%s' used for '%s' is not listed in DTB_OVERLAYS" % (ov, baseboard))
            if ov not in all:
                bb.warn("Overlay %s is not used by any baseboard" % ov)
                all += [ov]

    dtbdir = os.path.join(d.getVar('B'), d.getVar('KERNEL_OUTPUT_DIR'), "dts")

    def ov_file(ov):
        return os.path.join(dtbdir, ov)

    def get_syms(file, node):
        cmd = ['fdtget', '-p', '-d', "", file, node]
        syms = subprocess.check_output(cmd, text=True)
        if syms != None:
            return syms.split()
        return ()

    syms = dict()
    refs = dict()
    for f in d.getVar('KERNEL_DEVICETREE').split():
        syms[f] = get_syms(ov_file(f), "/__symbols__")
        refs[f] = get_syms(ov_file(f), "/__fixups__")

    ok = True
    for f in d.getVar('KERNEL_DEVICETREE').split():
        bb.note("Checking DTB '%s'" % f)
        for s in refs[f]:
            bb.debug(5, "Looking up %s from %s" % (s, f))
            if not s in list(itertools.chain.from_iterable(syms.values())):
                bb.error("'%s' referenced in '%s' is not defined in any DTB file" % (s, f))
                ok = False
            else:
                for (sf,ss) in syms.items():
                    if s in ss:
                        bb.debug(4, "%s referenced in %s is defined in %s" % (s, f, sf))
                        break

    if not ok:
        bb.fatal("check_overlays failed")
}
addtask do_check_overlays after do_compile_dtbs before do_compile
