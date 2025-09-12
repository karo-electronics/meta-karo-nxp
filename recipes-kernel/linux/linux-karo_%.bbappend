SRC_URI:append = " \
    file://karo-spidev-test.patch \
"

SRC_URI:append = "${@ "".join(map(lambda f: " file://dts/freescale/includes/%s-%s.dtsi;subdir=git/${KERNEL_OUTPUT_DIR}" % (d.getVar('SOC_PREFIX'), f), d.getVar('DTB_OVERLAY_INCLUDES').split()))}"

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

#FILES:${KERNEL_PACKAGE_NAME}-devicetree += "${@ " ".join(map(lambda f: f.replace("freescale/", "/boot/"), "${KERNEL_DEVICETREE}".split()))}"

require conf/machine/include/${SOC_PREFIX}-overlays.inc
