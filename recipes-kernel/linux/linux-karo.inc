# Copyright (C) 2012, 2015 O.S. Systems Software LTDA.
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "GPL-2.0-only"

inherit kernel fsl-vivante-kernel-driver-handler
LINUX_VERSION_EXTENSION ?= "-karo"

S = "${WORKDIR}/git"

do_symlink_kernsrc:append () {
    s = os.path.realpath(s)
    b = os.path.relpath(d.getVar("B"), s)
    os.symlink(b, os.path.join(s, 'build'))
}

python do_check_overlays() {
    import os
    import subprocess
    import itertools

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
