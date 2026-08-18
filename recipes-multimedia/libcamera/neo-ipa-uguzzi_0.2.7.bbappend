FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://patches/0001-data-add-imx219-support.patch"

SRC_URI:append = " \
    file://files/database_imx219_1920_1080.bin \
    file://files/database_imx219_3280_2464.bin \
"

do_configure:prepend() {
    cp ${UNPACKDIR}/files/database_imx219_3280_2464.bin ${S}/data/
    cp ${UNPACKDIR}/files/database_imx219_1920_1080.bin ${S}/data/
}
