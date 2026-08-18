require conf/distro/poky.conf

DISTRO_VERSION = "6.18-wrynose"
DISTROOVERRIDES = "karo"

MAINTAINER = "Ka-Ro <rl@karo-electronics.de>"

# Set a more generic tuning for code reuse across parts
DEFAULTTUNE:mx8-nxp-bsp    ?= "armv8a-crc-crypto"
DEFAULTTUNE:mx8m-nxp-bsp   ?= "armv8a-crc-crypto"
DEFAULTTUNE:mx8qm-nxp-bsp  ?= "armv8a-crc-crypto"
DEFAULTTUNE:mx8x-nxp-bsp   ?= "armv8a-crc-crypto"
DEFAULTTUNE:mx8ulp-nxp-bsp ?= "armv8a-crc-crypto"
DEFAULTTUNE:mx9-nxp-bsp    ?= "armv8-2a-crypto"
DEFAULTTUNE:mx91-nxp-bsp   ?= "armv8-2a-crypto"
DEFAULTTUNE:mx93-nxp-bsp   ?= "armv8-2a-crypto"
DEFAULTTUNE:mx943-nxp-bsp  ?= "armv8-2a-crypto"
DEFAULTTUNE:mx95-nxp-bsp   ?= "armv8-2a-crypto"

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
DISTRO_FEATURES_DEFAULTS:append:mx9-nxp-bsp = " edac"

# Use systemd as default init manager
POKY_INIT_MANAGER = "systemd"

PACKAGECONFIG:append:pn-gstreamer1.0-plugins-ugly = " x264"

TOOLCHAIN_HOST_TASK:append = " \
    nativesdk-binutils \
    nativesdk-gcc nativesdk-gcc-symlinks \
    nativesdk-gnutls-dev \
"
