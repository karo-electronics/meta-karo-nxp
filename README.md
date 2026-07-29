### Ka-Ro's Yocto layer for NXP i.MX official BSP ###

Adds support for the QS95-5610 module.

## Setup instructions

1. mkdir karo-imx-bsp
2. repo init -u https://github.com/nxp-imx/imx-manifest -b imx-linux-wrynose -m imx-6.18.20-2.0.0.xml
3. repo sync
4. cd sources
5. git clone ssh://git@buildserver-maxwell:2222/Ka-Ro/meta-karo-imx.git -b wrynose

## Example build instructions

1. DISTRO=fsl-imx-wayland-minimal MACHINE=qs95-5610 source imx-setup-release.sh -b build-karo
2. bitbake-layers add-layer ../sources/meta-karo-imx
3. bitbake imx-image-core

---
https://www.karo-electronics.de

Contact support: support@karo-electronics.de
