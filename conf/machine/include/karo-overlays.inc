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