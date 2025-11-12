SRC_URI:append = " \
    file://karo-spidev-test.patch \
"

SRC_URI:append = "${@ "".join(map(lambda f: " file://dts/freescale/includes/%s-%s.dtsi;subdir=git/${KERNEL_OUTPUT_DIR}" % (d.getVar('SOC_PREFIX'), f), d.getVar('DTB_OVERLAY_INCLUDES').split()))}"

#FILES:${KERNEL_PACKAGE_NAME}-devicetree += "${@ " ".join(map(lambda f: f.replace("freescale/", "/boot/"), "${KERNEL_DEVICETREE}".split()))}"

require conf/machine/include/${SOC_PREFIX}-overlays.inc

SRC_URI:append = " \
    file://ignore-build-dir.patch \
"
