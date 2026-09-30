FILESEXTRAPATHS:prepend:tx6-nand := "${THISDIR}/${PN}:"

# i.MX6 NAND ships fw_env.config here; elsewhere u-boot-karo-env provides it.
PACKAGE_ARCH:tx6-nand = "${MACHINE_ARCH}"

SRC_URI:append:tx6-nand = " file://fw_env_nand.config"

# poky libubootenv ships no fw_env.config; the U-Boot generator has no NAND line.
do_install:append:tx6-nand () {
    install -d ${D}${sysconfdir}
    install -m 644 ${WORKDIR}/fw_env_nand.config ${D}${sysconfdir}/fw_env.config
}
