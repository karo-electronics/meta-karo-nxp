# Overlay lists for i.MX95 modules; same model as the QS93 layer

DTB_OVERLAYS_generic ??= ""

DTB_OVERLAYS ??= ""

DTB_OVERLAYS:append:qs95 = " \
    qs95-enetc0-ksz9131 \
    qs95-enetc1-ksz9131 \
    qs95-pcie \
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

require karo-overlays.inc
