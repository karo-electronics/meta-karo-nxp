require karo-dtb-overlays.inc

DTB_OVERLAY_INCLUDES = "\
        karo-backlight \
        karo-dsi83 \
        karo-ft5x06 \
        karo-ksz9031 \
        karo-ksz9131 \
        karo-mcp2515 \
        karo-leds \
        karo-pcie \
        karo-sound \
"

# DTB overlays
DTB_OVERLAYS ??= ""

DTB_OVERLAYS_generic = "\
        ${@ mach_overlay(d, ":karo-dsi83 karo-gpu", "imx8mp")} \
        ${@ mach_overlay(d, "karo-mb7", "tx8m,tx8p")} \
        karo-rtc \
        ${@ mach_overlay(d, ":qs8m-tc358867", "tx8m,tx8p", "tc358867")} \
        tx8m-wifi \
        ${@ mach_overlay(d, ":karo-panel-et0570", "tx8m-1622")} \
        ${@ mach_overlay(d, ":karo-panel-etm0350", "tx8m-1622")} \
        ${@ mach_overlay(d, ":karo-panel-etm0430", "tx8m-1622")} \
        ${@ mach_overlay(d, ":karo-panel-etm0700", "tx8m-1622")} \
        ${@ mach_overlay(d, ":karo-panel-tm101jvhg32", "tx8m-1622")} \
        ${@ mach_overlay(d, ":qs8m-raspi-display", "tx8m,tx8p")} \
"

DTB_OVERLAYS:append = "\
        ${@ mach_overlay(d, "karo-copro:", dist="copro")} \
        ${@ mach_overlay(d, ":karo-vpu", "imx8mn")} \
"

DTB_OVERLAYS:append:imx8mp = "\
        karo-dsi83 \
        karo-gpu \
"

DTB_OVERLAYS:append:tx8m = "\
        karo-lvds-panel \
        tx8m-leds \
        tx8m-mcp2515 \
        ${@ mach_overlay(d, ":tx8m-pcie", "imx8mn")} \
        tx8m-sound \
"

DTB_OVERLAYS:append:tx8p = "\
        karo-lvds-panel \
        tx8p-flexcan \
        tx8p-ft5x06 \
        tx8p-lvds-mb \
        tx8p-mcp2515 \
        tx8p-sound \
"

DTB_OVERLAYS:append:tx8m-1620 = "\
        tx8m-1620-lvds-panel \
        tx8m-ft5x06 \
        tx8m-lvds-mb \
"

DTB_OVERLAYS:append:qs8m = "\
        ${@ mach_overlay(d, "qs8m-cam", dist="csi-camera")} \
        qs8m-ksz9031 \
        qs8m-ksz9131 \
"

DTB_OVERLAYS:append:qsxm = "\
        ${@ mach_overlay(d, "qs8m-cam", dist="csi-camera")} \
        karo-vpu \
        qs8m-ksz9031 \
        qs8m-ksz9131 \
        qsxm-laird \
        qsxm-pcie \
        qsxm-qsbase3 \
"

DTB_OVERLAYS:append:qsxp = "\
        ${@ mach_overlay(d, "qsxp-cam", dist="csi-camera")} \
        karo-vpu \
        qsxp-flexcan \
        qsxp-ksz9031 \
        qsxp-ksz9131 \
        qsxp-laird \
        qsxp-pcie \
        qsxp-qsbase3 \
        qsxp-raspi-display \
        qsxp-tc358867 \
"

KARO_BASEBOARDS:tx8m-1620 ?= "\
        lvds-mb \
        mb7 \
"

KARO_BASEBOARDS:tx8m-1622 ?= "\
        mipi-mb \
        mb7 \
"

KARO_BASEBOARDS:tx8m-1610 ?= "\
        mipi-mb \
        mb7 \
"

KARO_BASEBOARDS:tx8m-nd00 ?= "\
        mipi-mb \
        mb7 \
"

KARO_BASEBOARDS:tx8p ?= "\
        lvds-mb \
        mb7 \
"

KARO_BASEBOARDS:qs8m ?= "\
        qsbase2 \
        qsbase4 \
"

KARO_BASEBOARDS:qsxm ?= "\
        qsbase3 \
        qsbase4 \
"

KARO_BASEBOARDS:qsxp ?= "\
        qsbase3 \
        qsbase4 \
"

# baseboard DTB specific overlays
KARO_DTB_OVERLAYS[mb7] += "\
        ${@ mach_overlay(d, "karo-copro:", dist="copro")} \
        karo-gpu \
        karo-mb7 \
        ${@ mach_overlay(d, "tx8m-1620-lvds-panel", "tx8m-1620")} \
        ${@ mach_overlay(d, "karo-lvds-panel", "imx8mp")} \
        ${@ mach_overlay(d, "karo-panel-et0570,karo-panel-etm0350,karo-panel-etm0430,karo-panel-etm0700", "imx8mp,tx8m-1620")} \
        karo-rtc \
        ${@ mach_overlay(d, "tx8p-ft5x06:tx8m-ft5x06", "imx8mp")} \
        ${@ mach_overlay(d, ":tx8m-leds", "imx8mp")} \
        ${@ mach_overlay(d, "tx8p-sound:tx8m-sound", "imx8mp")} \
"

KARO_DTB_OVERLAYS[lvds-mb] += "\
        ${@ mach_overlay(d, "karo-copro:", dist="copro")} \
        karo-gpu \
        ${@ mach_overlay(d, "tx8m-1620-lvds-panel:karo-lvds-panel", "tx8m-1620")} \
        karo-panel-tm101jvhg32 \
        karo-rtc \
        ${@ mach_overlay(d, "tx8m-leds", "imx8mm")} \
        ${@ mach_overlay(d, "tx8p-lvds-mb:tx8m-lvds-mb", "imx8mp")} \
        ${@ mach_overlay(d, "tx8p-sound:tx8m-sound", "imx8mp")} \
        ${@ mach_overlay(d, "tx8m-wifi", "imx8mm", "wifi")} \
"

KARO_DTB_OVERLAYS[mipi-mb] += "\
        ${@ mach_overlay(d, "karo-copro:", dist="copro")} \
        ${@ mach_overlay(d, ":karo-dsi83", "tx8m-1622")} \
        karo-gpu \
        ${@ mach_overlay(d, ":karo-lvds-panel", "tx8m-1622")} \
        ${@ mach_overlay(d, ":karo-panel-tm101jvhg32", "tx8m-1622")} \
        karo-rtc \
        tx8m-leds \
        tx8m-sound \
        ${@ mach_overlay(d, "tx8m-wifi", "imx8mm", "wifi")} \
"

KARO_DTB_OVERLAYS[qsbase2] += "\
        ${@ mach_overlay(d, "karo-copro:", dist="copro")} \
        ${@ mach_overlay(d, "qs8m-cam", "imx8mm", "imx219")} \
        karo-gpu \
        qs8m-ksz9031 \
        ${@ mach_overlay(d, "qs8m-raspi-display", dist="raspi-display")} \
"

KARO_DTB_OVERLAYS[qsbase3] += "\
        ${@ mach_overlay(d, "karo-copro:", dist="copro")} \
        karo-gpu \
        ${@ mach_overlay(d, "qs8m-cam", "imx8mm", "csi-camera")} \
        ${@ mach_overlay(d, "qsxp-ksz9031:qs8m-ksz9031", "imx8mp")} \
        ${@ mach_overlay(d, "qsxp-laird:qsxm-laird", "imx8mp", "wifi")} \
        ${@ mach_overlay(d, "qsxp-pcie:qsxm-pcie", "imx8mp", "pcie")} \
        ${@ mach_overlay(d, "qsxp-qsbase3:qsxm-qsbase3", "imx8mp")} \
        ${@ mach_overlay(d, "qsxp-raspi-display:qs8m-raspi-display", "imx8mp", "raspi-display")} \
"

KARO_DTB_OVERLAYS[qsbase4] += "\
        ${@ mach_overlay(d, "karo-copro:", dist="copro")} \
        karo-gpu \
        ${@ mach_overlay(d, "qsxp-ksz9131:qs8m-ksz9131", "imx8mp")} \
        ${@ mach_overlay(d, "qsxp-raspi-display:qs8m-raspi-display", "imx8mp", "raspi-display")} \
"

KARO_DTB_OVERLAYS = "${@ d.getVarFlag('KARO_DTB_OVERLAYS', (d.getVar('KARO_BASEBOARD') or "None"), True) or ""}"
