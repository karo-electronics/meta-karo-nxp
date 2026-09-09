LICENSE = "MIT"

inherit core-image

IMAGE_FEATURES:append = " tools-debug"

DEBUG_TOOLS ?= " \
    gdbserver \
    i2c-tools \
    libgpiod \
    libgpiod-tools \
    spidev-test \
    strace \
    tcpdump \
"

IMAGE_INSTALL:append = " \
    bash \
    canutils \
    coreutils \
    ${DEBUG_TOOLS} \
    dosfstools \
    e2fsprogs \
    e2fsprogs-resize2fs \
    ethtool \
    file \
    kmod \
    ldd \
    less \
    libstdc++ \
    lsof \
    mmc-utils \
    ncurses \
    net-tools \
    nfs-utils \
    ntp \
    ntp-utils \
    parted \
    pciutils \
    rsync \
    sysfsutils \
    tzdata \
    util-linux-fsck \
    util-linux-mkfs \
    util-linux-mount \
    util-linux-setterm \
    v4l-utils \
"

IMAGE_INSTALL:append = " kernel-modules kernel-devicetree kernel-image"

WKS_FILE = "karo-imx-boot.wks.in"

do_create_uuu_script() {
    install -m 0644 ${THISDIR}/uuu-scripts/emmc_fb_all.lst ${DEPLOY_DIR_IMAGE}/
}
addtask do_create_uuu_script after do_image_complete before do_build
addtask do_unpack before do_create_uuu_script
