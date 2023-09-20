SRC_URI:append = " \
    file://drm-imx-dependency-bugfix.patch \
    file://0002-imx219-driver-zeus-version.patch \
"

SRC_URI:append:mx8-nxp-bsp = " \
    file://sec-dsim-bugfixes.patch \
    file://0001-display-support.patch \
    file://0003-vf610-gpio.patch \
    file://0002-panel-dpi-bus-format.patch \
    file://0017-spi-nand-dma-map-bugfix.patch \
    file://0019-fdt5x06-dma-bugfix.patch \
    file://0001-mx6s-capture-add-rggb8-video-format.patch \
    file://0003-isi-fmts.patch \
    file://0004-csi-fmts.patch \
"

SRC_URI:append:mx9-nxp-bsp = " \
    file://imx-intmux-dependency-bugfix.patch \
    file://dont-select-imx-gpcv2.patch \
    file://imx93-dtsi-bugfixes.patch \
    file://lpspi-dmas.patch \
    file://imx93-eqos-rmii-workaround.patch \
    file://imx93-tpm-bugfix.patch \
    file://imx93-pll-rate-bugfix.patch \
    file://karo-spidev-test.patch \
"

SRC_URI:append:qs93 = " \
    file://0001-imx93-add-support-for-raw-format-cameras-based-on-L6.patch \
    file://0002-imx219-test-enable-embedded-data-support-and-MSB-ali.patch \
"

SRC_URI:append = "${@ "".join(map(lambda f: " file://dts/freescale/overlays/%s-%s.dtsi;subdir=git/${KERNEL_OUTPUT_DIR}" % (d.getVar('SOC_PREFIX'), f), d.getVar('DTB_OVERLAY_INCLUDES').split()))}"

def get_overlays(d):
    ovlist = []
    g = d.getVar('DTB_OVERLAYS_generic') or ""
    o = d.getVar('DTB_OVERLAYS') or ""
    for ov in g.split():
        for fn in ov.split(","):
            fn = " freescale/%s-%s.dtb" % (d.getVar('SOC_PREFIX'), fn)
            if fn in ovlist:
                bb.debug(2, "'%s' is already in overlays" % fn)
            else:
                bb.debug(2, "Adding '%s' to overlays" % fn)
                ovlist += [fn]

    for ov in o.split():
        for fn in ov.split(","):
            fn = " freescale/%s-%s.dtb" % (d.getVar('SOC_FAMILY'), fn)
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
