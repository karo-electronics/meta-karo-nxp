FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/patches:"

SRC_URI:append = " \
    file://0001-isp-imx-add-imx219.patch \
    file://0002-isp-imx-make-imx219-default.patch \
" 

FILES_SOLIBS_VERSIONED:append = " \
    ${libdir}/libimx219.so \
"
