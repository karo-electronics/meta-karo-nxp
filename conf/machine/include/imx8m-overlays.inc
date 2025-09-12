require karo-dtb-overlays.inc

DTB_OVERLAY_INCLUDES = "\
        karo-backlight \
        karo-dsi83 \
        karo-ksz9031 \
        karo-ksz9131 \
        karo-mcp2515 \
        karo-leds \
        karo-pcie \
        karo-sound \
        karo-raspi-display \
"

# DTB overlays
DTB_OVERLAYS ??= ""

DTB_OVERLAYS_generic = "\
        ${@ mach_overlay(d, ":karo-dsi83 karo-gpu", "imx8mp")} \
        ${@ mach_overlay(d, "karo-mb7", "tx8m,tx8p")} \
        karo-rtc \
        ${@ mach_overlay(d, "qs8m-tc358867", "qs8m,qsxm", "tc358867")} \
        ${@ mach_overlay(d, ":karo-panel-et0570", "qs8m,qsxm,tx8m-1622")} \
        ${@ mach_overlay(d, ":karo-panel-etm0350", "qs8m,qsxm,tx8m-1622")} \
        ${@ mach_overlay(d, ":karo-panel-etm0430", "qs8m,qsxm,tx8m-1622")} \
        ${@ mach_overlay(d, ":karo-panel-etm0700", "qs8m,qsxm,tx8m-1622")} \
        ${@ mach_overlay(d, ":karo-panel-tm101jvhg32", "qs8m,qsxm,tx8m-1622")} \
"

DTB_OVERLAYS:append = "\
        ${@ mach_overlay(d, "karo-copro", dist="copro")} \
        ${@ mach_overlay(d, ":karo-vpu", "imx8mn")} \
        ${@ mach_overlay(d, ":tx8m-wifi", "imx8mn")} \
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
        tx8m-spidev \
"

DTB_OVERLAYS:append:tx8p = "\
        karo-lvds-panel \
        tx8p-flexcan \
        tx8p-pcie \
        tx8p-ft5x06 \
        tx8p-lvds-mb \
        tx8p-mcp2515 \
        tx8p-sound \
        tx8p-spidev \
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
        qs8m-raspi-display \
        qs8m-spidev \
"

DTB_OVERLAYS:append:qsxm = "\
        ${@ mach_overlay(d, "qs8m-cam", dist="csi-camera")} \
        karo-vpu \
        qs8m-ksz9031 \
        qs8m-ksz9131 \
        qs8m-raspi-display \
        qs8m-spidev \
        qsxm-laird \
        qsxm-pcie \
        qsxm-qsbase3 \
"

DTB_OVERLAYS:append:qsxp = "\
        karo-vpu \
        ${@ mach_overlay(d, "qsxp-cam", dist="csi-camera")} \
        qsxp-flexcan \
        qsxp-ksz9031 \
        qsxp-ksz9131 \
        qsxp-laird \
        ${@ mach_overlay(d, "qsxp-mipi-lvds-bridge", dist="dsi83")} \
        qsxp-pcie \
        qsxp-qsbase3 \
        qsxp-raspi-display \
        qsxp-spidev \
        ${@ mach_overlay(d, "qsxp-tc358867", dist="tc358867")} \
"

KARO_BASEBOARDS:tx8m-1620 ?= "\
        lvds-mb \
        mb7 \
"

KARO_BASEBOARDS:tx8m-1622 ?= "\
        mb7 \
        mipi-mb \
"

KARO_BASEBOARDS:tx8m-1610 ?= "\
        mb7 \
        mipi-mb \
"

KARO_BASEBOARDS:tx8m-nd00 ?= "\
        mb7 \
        mipi-mb \
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
        ${@ mach_overlay(d, "karo-copro", dist="copro")} \
        karo-gpu \
        karo-mb7 \
        ${@ mach_overlay(d, "karo-lvds-panel", "imx8mp")} \
        ${@ mach_overlay(d, "tx8m-1620-lvds-panel", "tx8m-1620")} \
        ${@ mach_overlay(d, "karo-panel-et0570,karo-panel-etm0350,karo-panel-etm0430,karo-panel-etm0700", "imx8mp,tx8m-1620")} \
        ${@ mach_overlay(d, "tx8p-flexcan", "imx8mp", "flexcan")} \
        ${@ mach_overlay(d, "tx8m-ft5x06", "tx8m-1620")} \
        ${@ mach_overlay(d, ":tx8m-leds", "imx8mp")} \
        ${@ mach_overlay(d, "tx8p-ft5x06", "imx8mp")} \
        ${@ mach_overlay(d, "tx8p-sound:tx8m-sound", "imx8mp")} \
        ${@ mach_overlay(d, "tx8p-spidev:tx8m-spidev", "imx8mp")} \
"

KARO_DTB_OVERLAYS[lvds-mb] += "\
        ${@ mach_overlay(d, "karo-copro", dist="copro")} \
        karo-gpu \
        ${@ mach_overlay(d, "tx8m-1620-lvds-panel:karo-lvds-panel", "tx8m-1620")} \
        karo-panel-tm101jvhg32 \
        karo-rtc \
        ${@ mach_overlay(d, "tx8m-leds", "imx8mm")} \
        ${@ mach_overlay(d, "tx8m-wifi", "imx8mm", "wifi")} \
        ${@ mach_overlay(d, "tx8p-lvds-mb:tx8m-lvds-mb", "imx8mp")} \
        ${@ mach_overlay(d, "tx8p-sound:tx8m-sound", "imx8mp")} \
        ${@ mach_overlay(d, "tx8p-spidev:tx8m-spidev", "imx8mp")} \
        ${@ mach_overlay(d, "tx8p-pcie:tx8m-pcie", "imx8mp")} \
"

KARO_DTB_OVERLAYS[mipi-mb] += "\
        ${@ mach_overlay(d, "karo-copro", dist="copro")} \
        ${@ mach_overlay(d, ":karo-dsi83", "tx8m-1622")} \
        karo-gpu \
        ${@ mach_overlay(d, ":karo-lvds-panel", "tx8m-1622")} \
        ${@ mach_overlay(d, ":karo-panel-tm101jvhg32", "tx8m-1622")} \
        karo-rtc \
        tx8m-leds \
        tx8m-sound \
        tx8m-spidev \
        ${@ mach_overlay(d, "tx8m-wifi", "imx8mm", "wifi")} \
"

KARO_DTB_OVERLAYS[qsbase2] += "\
        ${@ mach_overlay(d, "karo-copro", dist="copro")} \
        karo-gpu \
        ${@ mach_overlay(d, "qs8m-cam", "imx8mm", "imx219")} \
        qs8m-ksz9031 \
        ${@ mach_overlay(d, "qs8m-raspi-display", dist="raspi-display")} \
        qs8m-spidev \
"

KARO_DTB_OVERLAYS[qsbase3] += "\
        ${@ mach_overlay(d, "karo-copro", dist="copro")} \
        karo-gpu \
        ${@ mach_overlay(d, "qsxp-flexcan", dist="flexcan")} \
        ${@ mach_overlay(d, "qs8m-cam", "imx8mm", "csi-camera")} \
        ${@ mach_overlay(d, "qsxp-ksz9031:qs8m-ksz9031", "imx8mp")} \
        ${@ mach_overlay(d, "qsxp-laird:qsxm-laird", "imx8mp", "wifi")} \
        ${@ mach_overlay(d, "qsxp-pcie:qsxm-pcie", "imx8mp", "pcie")} \
        ${@ mach_overlay(d, "qsxp-qsbase3:qsxm-qsbase3", "imx8mp")} \
        ${@ mach_overlay(d, "qsxp-raspi-display:qs8m-raspi-display", "imx8mp", "raspi-display")} \
        ${@ mach_overlay(d, "qsxp-spidev:qs8m-spidev", "imx8mp")} \
"

KARO_DTB_OVERLAYS[qsbase4] += "\
        ${@ mach_overlay(d, "karo-copro", dist="copro")} \
        karo-gpu \
        ${@ mach_overlay(d, "qsxp-flexcan", dist="flexcan")} \
        ${@ mach_overlay(d, "qsxp-ksz9131:qs8m-ksz9131", "imx8mp")} \
        ${@ mach_overlay(d, "qsxp-raspi-display:qs8m-raspi-display", "imx8mp", "raspi-display")} \
        ${@ mach_overlay(d, "qsxp-spidev:qs8m-spidev", "imx8mp")} \
"

KARO_DTB_OVERLAYS = "${@ d.getVarFlag('KARO_DTB_OVERLAYS', (d.getVar('KARO_BASEBOARD') or "None"), True) or ""}"
