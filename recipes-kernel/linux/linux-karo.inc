# Copyright (C) 2012, 2015 O.S. Systems Software LTDA.
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "GPL-2.0-only"

inherit kernel fsl-vivante-kernel-driver-handler
LINUX_VERSION_EXTENSION ?= "-karo"

S = "${WORKDIR}/git"

def get_overlays(d):
    ovlist = []
    g = d.getVar('DTB_OVERLAYS_generic') or ""
    o = d.getVar('DTB_OVERLAYS') or ""
    for ov in g.split():
        for fn in ov.split(","):
            fn = " freescale/%s-%s.dtbo" % (d.getVar('SOC_PREFIX'), fn)
            if fn in ovlist:
                bb.debug(2, "'%s' is already in overlays" % fn)
            else:
                bb.debug(2, "Adding '%s' to overlays" % fn)
                ovlist += [fn]

    for ov in o.split():
        for fn in ov.split(","):
            fn = " freescale/%s-%s.dtbo" % (d.getVar('SOC_FAMILY'), fn)
            if fn in ovlist:
                bb.debug(2, "'%s' is already in overlays" % fn)
            else:
                bb.debug(2, "Adding '%s' to overlays" % fn)
                ovlist += [fn]

    if len(ovlist) == 0:
        return ""
    return " ".join(ovlist)

KERNEL_DEVICETREE:append = "${@ get_overlays(d) }"

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
