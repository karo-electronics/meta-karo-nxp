require karo-dtb-overlays.inc

DTB_OVERLAY_INCLUDES = " \
        karo-backlight \
        karo-ft5x06 \
        karo-ili2130 \
        karo-mcp2515 \
        karo-leds \
        karo-pcie \
        karo-sound \
"

DTB_OVERLAYS_generic = "\
        karo-lcd-panel \
        karo-lvds-mb \
        karo-rtc \
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

DTB_OVERLAYS:append:tx91 = " \
        tx91-flexcan \
        tx91-ft5x06 \
        tx91-ili2130 \
        tx91-sound \
        tx91-spidev \
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
KARO_DTB_OVERLAYS[mb7] += " \
        karo-lcd-panel \
        karo-mb7 \
        tx91-ft5x06,tx91-ili2130 \
        tx91-sound \
"

KARO_DTB_OVERLAYS[lvds-mb] += " \
        karo-lvds-mb \
        karo-panel-tm101jvhg32 \
        karo-rtc \
"
