require karo-dtb-overlays.inc

# Display panels are overlays; the IPU SoCs also drive LVDS.
DTB_OVERLAYS_generic = "\
        karo-panel-etm0700 \
"
DTB_OVERLAYS_generic:append:mx6q-nxp-bsp = " karo-lvds-panel-etml1010"
DTB_OVERLAYS_generic:append:mx6dl-nxp-bsp = " karo-lvds-panel-etml1010"

DTB_OVERLAYS ??= ""
DTB_OVERLAY_INCLUDES ??= ""

KARO_BASEBOARDS ?= "mb7"

# MB7 default: the RGB panel on TTL modules, LVDS on x1xx.
KARO_DTB_OVERLAYS[mb7] += " \
        ${@ mach_overlay(d, "karo-panel-etm0700:karo-lvds-panel-etml1010", "tx6s-8034,tx6s-8035,tx6u-8030,tx6u-8033,tx6q-1030,tx6q-1036,tx6q-8037,txul-5010,txul-5011,txul-8013")} \
"

KERNEL_DEVICETREE:append = "${@ get_overlays(d) }"
