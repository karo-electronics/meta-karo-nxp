require karo-overlays.inc

DTB_OVERLAYS_generic ??= ""

DTB_OVERLAYS ??= ""

DTB_OVERLAYS:append:qs95 = " \
    qs95-enetc0-ksz9131 \
    qs95-enetc1-ksz9131 \
    qs95-pcie \
    qs95-copro \
    qs95-cam \
    qs95-lvds-panel \
    qs95-raspi-display \
"

KARO_BASEBOARDS ??= ""

KARO_BASEBOARDS:qs95 += " \
    qsbase95 \
"

# KARO_DTB_OVERLAYS[<baseboard>] lists the overlays loaded by default there
KARO_DTB_OVERLAYS[qsbase95] += " \
    qs95-enetc0-ksz9131 \
    qs95-enetc1-ksz9131 \
    qs95-pcie \
    qs95-raspi-display \
"

KERNEL_DEVICETREE:append = "${@ get_overlays(d) }"
