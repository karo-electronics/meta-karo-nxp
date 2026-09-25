# Ka-Ro TX6 modules: i.MX6 UL/ULL/S/DL/Q, DCD-only boot, no SPL.

MACHINEOVERRIDES =. "mx6:"

require karo-base.inc

# Monolithic DCD image; karo-base.inc and imx-base.inc assume SPL + u-boot.bin.
UBOOT_MAKE_TARGET = "u-boot-dtb.imx"
UBOOT_SUFFIX = "imx"
UBOOT_BINARY = "u-boot-dtb.imx"
SPL_BINARY = ""

# Device trees come from the kernel tree.

# Empty by design; otherwise karo-base.inc appends a pmic fragment.
KARO_BOARD_PMIC = ""

# DTB paths under nxp/imx/, built from the kernel tree.
KERNEL_DEVICETREE = "nxp/imx/${DTB_BASENAME}.dtb nxp/imx/${UBOOT_DTB_NAME}.dtb"

# ARM32 boots a uImage; U-Boot on i.MX6 has bootm only.
KERNEL_IMAGETYPE:mx6-nxp-bsp = "uImage"

# Ka-Ro device trees live under nxp/imx in the kernel.
DTB_VENDOR_DIR = "nxp/imx"
# CI builds once without baseboard; TX6 is only qualified on MB7.
KARO_BASEBOARD := "${@d.getVar('KARO_BASEBOARD') or 'mb7'}"

require imx6-overlays.inc
