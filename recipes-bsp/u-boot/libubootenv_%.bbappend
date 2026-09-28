FILESEXTRAPATHS:prepend:mx6-nxp-bsp := "${THISDIR}/${PN}:"

# i.MX6 ships fw_env.config here; U-Boot provides it elsewhere.
PACKAGE_ARCH:mx6-nxp-bsp = "${MACHINE_ARCH}"

KARO_FW_ENV_STORAGE = "${@bb.utils.contains('MACHINE_FEATURES', 'nand', 'nand', 'emmc', d)}"
SRC_URI:append:mx6-nxp-bsp = " file://fw_env_${KARO_FW_ENV_STORAGE}.config"

# Install our file by name: another layer may drop a fw_env.config in WORKDIR.
do_install:append:mx6-nxp-bsp () {
    install -d ${D}${sysconfdir}
    install -m 644 ${WORKDIR}/fw_env_${KARO_FW_ENV_STORAGE}.config ${D}${sysconfdir}/fw_env.config
}
