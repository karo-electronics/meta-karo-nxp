SRC_URI:append = " \
        file://sn65dsi83-gpio-optional.patch \
        file://sec-dsim-bugfixes.patch \
"

SRC_URI:append:mx8m-nxp-bsp = "${@ "".join(map(lambda f: " file://dts/freescale/overlays/%s-%s.dtsi;subdir=git/${KERNEL_OUTPUT_DIR}" % (d.getVar('SOC_PREFIX'), f), d.getVar('DTB_OVERLAY_INCLUDES').split()))}"

def get_overlays(d):
    ovlist = []
    generic = d.getVar('DTB_OVERLAYS_generic') or ""
    g = generic.split()
    bb.debug(2, "KARO_BASEBOARDS='%s'" % d.getVar('KARO_BASEBOARDS'))
    bb.debug(2, "generic='%s'" % " ".join(generic.split()))
    if d.getVar('KARO_BASEBOARDS') != None:
        for b in d.getVar('KARO_BASEBOARDS').split():
            bb.debug(2, "baseboard='%s'" % b)
            o = d.getVarFlag('KARO_DTB_OVERLAYS', b, True) or ()
            bb.debug(2, "DTB_OVERLAYS[%s]='%s'" % (b, " ".join(o.split())))
            for ov in o.split():
                bb.debug(2, "ov='%s'" % ov)
                for fn in ov.split(","):
                    bb.debug(2, "fn='%s'" % fn)
                    if fn in g:
                        fn = " freescale/%s-%s.dtb" % (d.getVar('SOC_PREFIX'), fn)
                        if fn in ovlist:
                            bb.debug(2, "'%s' is already in overlays" % fn)
                        else:
                            bb.debug(2, "Adding '%s' to overlays" % fn)
                            ovlist += [fn]
                    else:
                        fn = " freescale/%s-%s.dtb" % (d.getVar('SOC_FAMILY'), fn)
                        bb.debug(2, "Adding '%s' to overlays" % fn)
                        ovlist += [fn]
    else:
        bb.warn("KARO_BASEBOARDS is not set")

    if len(ovlist) == 0:
        return ""
    bb.debug(2, "ovlist='%s'" % ovlist)
    return " ".join(ovlist)

KERNEL_DEVICETREE:append = "${@ get_overlays(d) }"

#FILES:${KERNEL_PACKAGE_NAME}-devicetree += "${@ " ".join(map(lambda f: f.replace("freescale/", "/boot/"), "${KERNEL_DEVICETREE}".split()))}"

require conf/machine/include/${SOC_PREFIX}-overlays.inc
