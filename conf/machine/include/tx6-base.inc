require conf/machine/include/imx-base.inc

KBUILD_BUILD_USER := "support"
KBUILD_BUILD_HOST := "karo-electronics.de"

UBOOT_ENTRYPOINT_mx6  = "0x10008000"
UBOOT_ENTRYPOINT_mx6ul = "0x80008000"

DEFAULTTUNE_mx6 ?= "cortexa9hf-neon"
DEFAULTTUNE_mx6ul ?= "cortexa7hf-neon"

PREFERRED_PROVIDER_virtual/xserver = "xserver-xorg"
XSERVER_DRIVER = "xf86-video-fbdev"
XSERVER_DRIVER_mx6 = "xf86-video-modesetting"
XSERVER = "xserver-xorg \
           xf86-input-evdev \
           ${XSERVER_DRIVER}"

PREFERRED_PROVIDER_virtual/egl ?= "mesa"
PREFERRED_PROVIDER_virtual/libgles1 ?= "mesa"
PREFERRED_PROVIDER_virtual/libgles2 ?= "mesa"
PREFERRED_PROVIDER_virtual/libgl ?= "mesa"
PREFERRED_PROVIDER_virtual/libg2d_mx6 ?= "mesa"
PREFERRED_PROVIDER_virtual/libg2d_mx6ul ?= ""

PREFERRED_PROVIDER_virtual/kernel ??= "linux-imx"
PREFERRED_VERSION_linux-imx ?= "4.14.98%"
KERNEL_IMAGETYPE = "uImage"
KERNEL_FEATURES += "wifi systemd"

#MACHINE_ESSENTIAL_EXTRA_RDEPENDS = " \
#                 linux-firmware-imx-sdma-imx6q \
#"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS_append_karo-x11 = " \
                 linux-firmware-imx-vpu \
"

SERIAL_CONSOLES = "115200;ttymxc0"
SERIAL_CONSOLES_mxs = "115200;ttyAMA0"

MACHINE_FEATURES += " \
                 alsa \
                 rtc \
                 serial \
                 touchscreen \
                 usbgadget \
                 usbhost \
                 vfat \
"

# Remove some image types we don't need
IMAGE_FSTYPES_remove = " sdcard"

PREFERRED_VERSION_imx-test = "00.00.00"