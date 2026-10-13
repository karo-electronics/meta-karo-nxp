require karo-dtb-overlays.inc

# MB7 carrier and display panels are overlays.
# imx6qdl-tx6-lvds.dtsi: lvds0_panel/lvds1_panel exist only on LVDS modules, lcd_panel only on RGB ones.
DTB_OVERLAYS_generic = "\
        karo-mb7 \
        ${@bb.utils.contains('MACHINE_FEATURES', 'lvds', 'karo-lvds-panel-etml1010', 'karo-panel-etm0700', d)} \
"
# ULL panel overlay uses iomuxc_snvs: name it per family.
DTB_OVERLAYS_generic:remove:mx6ull-nxp-bsp = "karo-panel-etm0700"
DTB_OVERLAYS:append:mx6ull-nxp-bsp = " karo-panel-etm0700"

DTB_OVERLAYS ??= ""
DTB_OVERLAY_INCLUDES ??= ""

KARO_BASEBOARDS ?= "mb7"

# MB7 default: carrier first, then LVDS panel on LVDS modules, RGB panel elsewhere.
KARO_DTB_OVERLAYS[mb7] += " \
        karo-mb7 \
        ${@bb.utils.contains('MACHINE_FEATURES', 'lvds', 'karo-lvds-panel-etml1010', 'karo-panel-etm0700', d)} \
"

KERNEL_DEVICETREE:append = "${@ get_overlays(d) }"
