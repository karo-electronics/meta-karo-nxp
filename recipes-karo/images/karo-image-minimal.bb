SUMMARY = "A minimal Linux system without graphics support."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

require karo-image.inc

IMAGE_LINGUAS = ""

IMAGE_FEATURES:remove = "tools-debug"

# remove packages that pull in openssl
IMAGE_INSTALL:remove = "curl git"

python extend_recipe_sysroot:append() {
    if d.getVar('DISTRO') != 'karo-imx-minimal':
        raise_sanity_error("cannot build '%s' with DISTRO '%s'" % (d.getVar('BPN'), d.getVar('DISTRO')), d)
}

export IMAGE_BASENAME = "karo-image-minimal"
