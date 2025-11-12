require karo-dtb-overlays.inc

DTB_OVERLAY_INCLUDES = " \
        karo-backlight \
        karo-ili2130 \
        karo-mcp2515 \
        karo-leds \
        karo-pcie \
        karo-sound \
"

DTB_OVERLAYS_generic = "\
        karo-copro \
        karo-gpu \
        karo-lcd-panel \
        karo-panel-et0570 \
        karo-panel-etm0430 \
        karo-panel-etm0350 \
        karo-panel-etm0700 \
        karo-panel-tm101jvhg32 \
        karo-npu \
        karo-rtc \
"

DTB_OVERLAYS_generic:append:tx93 = "\
        karo-lvds-mb \
        karo-mb7 \
"

DTB_OVERLAYS ??= ""

DTB_OVERLAYS:append:tx93 = " \
        tx93-flexcan \
        tx93-ft5x06 \
        tx93-ili2130 \
        tx93-lvds-panel \
        tx93-matrix-keypad \
        tx93-sound \
        tx93-spidev \
"

DTB_OVERLAYS:append:qs93 = " \
        qs93-eqos-ksz9031 \
        qs93-eqos-ksz9131 \
        qs93-eqos-lan8710 \
        qs93-fec-lan8710 \
        qs93-flexcan1 \
        qs93-flexcan2 \
        qs93-imx219 \
        qs93-lvds-panel \
        qs93-sound \
        qs93-spidev \
"

KARO_BASEBOARDS:tx93 += "\
        lvds-mb \
        mb7 \
"
KARO_BASEBOARDS:qs93 += "\
        qsbase4 \
        qsbase93 \
"

#
# KARO_DTB_OVERLAYS[<baseboard>] lists all overlays that will be
# included in the default list of overlays to be loaded for a specific
# baseboard.
#
KARO_DTB_OVERLAYS[mb7] += " \
        ${@ mach_overlay(d, "karo-copro", dist="copro")} \
        karo-gpu \
        karo-lcd-panel karo-panel-et0570,karo-panel-etm0430,karo-panel-etm0350,karo-panel-etm0700,karo-panel-tm101jvhg32 \
        karo-mb7 \
        ${@ mach_overlay(d, "tx93-flexcan", dist="flexcan")} \
        tx93-ft5x06,tx93-ili2130 \
        tx93-matrix-keypad \
        tx93-sound \
"

KARO_DTB_OVERLAYS[lvds-mb] += " \
        ${@ mach_overlay(d, "karo-copro", dist="copro")} \
        karo-gpu \
        karo-lvds-mb \
        tx93-lvds-panel karo-panel-tm101jvhg32 \
        karo-rtc \
"

KARO_DTB_OVERLAYS[qsbase4] += " \
        ${@ mach_overlay(d, "karo-copro", dist="copro")} \
        qs93-eqos-ksz9131 \
        ${@ mach_overlay(d, "qs93-flexcan1", dist="flexcan")} \
        ${@ mach_overlay(d, "qs93-flexcan2", dist="flexcan")} \
"

KARO_DTB_OVERLAYS[qsbase93] += " \
        ${@ mach_overlay(d, "karo-copro", dist="copro")} \
        karo-gpu \
        qs93-eqos-lan8710 \
        qs93-fec-lan8710 \
        qs93-lvds-panel karo-panel-tm101jvhg32 \
"
