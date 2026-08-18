### Ka-Ro's Yocto layer for NXP i.MX official BSP ###

This layer adds support for the QS95-5610 module.
It is made to work with the official NXP i.MX Yocto BSP.

- **Yocto Version**: `wrynose`
- **NXP BSP Version**: `imx-6.18.20-2.0.0`

## Setup instructions

1. `mkdir karo-imx-bsp`
2. `repo init -u https://github.com/nxp-imx/imx-manifest -b imx-linux-wrynose -m imx-6.18.20-2.0.0.xml`
3. `repo sync`
4. `cd sources`
5. `git clone ssh://git@buildserver-maxwell:2222/Ka-Ro/meta-karo-imx.git -b wrynose`

## Example build instructions

1. `DISTRO=karo-imx-wayland MACHINE=qs95-5610 source imx-setup-release.sh -b build-karo`
2. `bitbake-layers add-layer ../sources/meta-karo-imx`
3. `bitbake karo-image-weston`

## Example flash instructions

1. Put your board into download mode or activate fastboot in u-boot
2. Move into the deploy image directory (e.g. `cd ${BUILD_DIR}/tmp/deploy/images/qs95-5610`)
3. `uuu -v -b emmc_fb_all.lst karo-image-minimal-qs95-5610.rootfs.wic.zst`

---
Website: https://www.karo-electronics.de

Contact support: support@karo-electronics.de
