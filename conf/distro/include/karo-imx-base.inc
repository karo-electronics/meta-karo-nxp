require conf/distro/poky.conf

DISTRO_VERSION = "6.18-wrynose"
DISTROOVERRIDES = "karo"

MAINTAINER = "Ka-Ro <rl@karo-electronics.de>"

# Set a more generic tuning for code reuse across parts
DEFAULTTUNE:mx9-nxp-bsp    ?= "armv8-2a-crypto"

SKIP_META_SECURITY_SANITY_CHECK = "1"
SKIP_META_TPM_SANITY_CHECK = "1"

POKY_DEFAULT_DISTRO_FEATURES:remove = "\
    largefile \
    opengl \
    ptest \
    vulkan \
    wayland \
"

DISTRO_FEATURES_DEFAULTS ?= "\
    acl \
    alsa \
    argp \
    dhcpcd \
    ext2 \
    ipv4 \
    largefile \
    ldconfig \
    pam \
    nfs \
    seccomp \
    telnet-login \
    u-boot-fw-utils \
    usbgadget \
    usbhost \
    vfat \
    xattr \
"
DISTRO_FEATURES_DEFAULTS:append = " pcie dsi83 lvds-panel raspi-display csi-camera imx219"

# Use systemd as default init manager
POKY_INIT_MANAGER = "systemd"

PACKAGECONFIG:append:pn-gstreamer1.0-plugins-ugly = " x264"

TOOLCHAIN_HOST_TASK:append = " \
    nativesdk-binutils \
    nativesdk-gcc nativesdk-gcc-symlinks \
    nativesdk-gnutls-dev \
"

IMX_MALI_DRIVERS ??= "mali-imx mesa"

PACKAGECONFIG:append:pn-pulseaudio = " autospawn-for-root"

PACKAGECONFIG:append:pn-mesa-demos = " wayland"
PACKAGECONFIG:remove:pn-mesa-demos = "vulkan"
