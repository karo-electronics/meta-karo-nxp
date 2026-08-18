LICENSE = "MIT"

def do_copyfile (src, destdir):
    bb.note("Copying %s to %s" % (src, destdir))
    if os.system("cp %s %s" % (src, destdir)):
        bb.fatal("Failed to copy %s to %s" % (src, destdir))

inherit core-image

FILESEXTRAPATHS:prepend := "${THISDIR}/uuu-scripts:"

SRC_URI:append = " file://emmc_fb_all.lst"

python () {
    # Needed for template files
    d.delVarFlag('do_unpack', 'noexec')
}

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

python do_create_uuu_script() {
    img_dirname = d.getVar('DEPLOY_DIR_IMAGE')
    script_path = os.path.join(d.getVar('UNPACKDIR'), "emmc_fb_all.lst")
    do_copyfile(script_path, img_dirname)
}
addtask do_create_uuu_script after do_image_complete before do_build
addtask do_unpack before do_create_uuu_script
