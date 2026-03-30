require karo-dtb-overlays.inc

DTB_OVERLAY_INCLUDES = " \
        karo-backlight \
        karo-lcd-panel \
        karo-ili2130 \
        karo-mcp2515 \
        karo-leds \
        karo-sound \
"

DTB_OVERLAYS_generic = "\
        karo-panel-et0570 \
        karo-panel-etm0430 \
        karo-panel-etm0350 \
        karo-panel-etm0700 \
        karo-panel-tm101jvhg32 \
"

DTB_OVERLAYS_generic:append:tx91 = "\
        karo-mb7 \
"

DTB_OVERLAYS ??= ""

DTB_OVERLAYS:append:qs91 = " \
        qs91-eqos-ksz9031 \
        qs91-eqos-ksz9131 \
        qs91-ft5x06 \
        qs91-i2c-rtc \
        qs91-ili2130 \
        qs91-lcd-panel \
        qs91-qsbase1 \
        qs91-sound \
"

DTB_OVERLAYS:append:tx91 = " \
        tx91-flexcan \
        tx91-ft5x06 \
        tx91-i2c-rtc \
        tx91-ili2130 \
        tx91-lcd-panel \
        tx91-matrix-keypad \
        tx91-sound \
        tx91-spidev \
"

KARO_BASEBOARDS:qs91 += "\
        qsbase1 \
        qsbase4 \
"

KARO_BASEBOARDS:tx91 += "\
        lvds-mb \
        mb7 \
"

#
# KARO_DTB_OVERLAYS[<baseboard>] lists all overlays that will be
# included in the default list of overlays to be loaded for a specific
# baseboard.
#
KARO_DTB_OVERLAYS[lvds-mb] += " \
        tx91-i2c-rtc \
"

KARO_DTB_OVERLAYS[mb7] += " \
        tx91-lcd-panel \
        karo-panel-et0570,karo-panel-etm0430,karo-panel-etm0350,karo-panel-etm0700,karo-panel-tm101jvhg32 \
        karo-mb7 \
        ${@ mach_overlay(d, "tx91-flexcan", dist="flexcan")} \
        tx91-ft5x06,tx91-ili2130 \
        tx91-matrix-keypad \
        tx91-sound \
"

KARO_DTB_OVERLAYS[qsbase4] += " \
        qs91-eqos-ksz9131 \
"

KARO_DTB_OVERLAYS[qsbase1] += " \
        qs91-eqos-ksz9031 \
        qs91-lcd-panel \
        karo-panel-et0570,karo-panel-etm0430,karo-panel-etm0350,karo-panel-etm0700,karo-panel-tm101jvhg32 \
        qs91-qsbase1 \
        qs91-ft5x06,qs91-ili2130 \
"
