FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/patches:"

SRC_URI:append = " \
    file://0001-isp-vvcam-add-imx219.patch \
"
