FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://devtool-fragment.cfg file://0001-dts-add-dts-files-for-qs95-5610-module-and-qsbase95-.patch"

# Create symlink to fix path resolution error when using devtool
do_kernel_metadata:prepend() {
    if [ ! -e "${WORKDIR}/sources" ]; then
        ln -sf . "${WORKDIR}/sources"
    fi
}
