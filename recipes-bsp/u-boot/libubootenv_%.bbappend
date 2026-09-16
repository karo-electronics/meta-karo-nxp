FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# i.MX6 ships fw_env.config here; U-Boot provides it elsewhere.
PACKAGE_ARCH:mx6-nxp-bsp = "${MACHINE_ARCH}"

SRC_URI:append:mx6-nxp-bsp = " file://fw_env_${@bb.utils.contains('MACHINE_FEATURES', 'nand', 'nand', 'emmc', d)}.config"

do_install:append:mx6-nxp-bsp () {
    install -d ${D}${sysconfdir}
    install -m 644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
}
