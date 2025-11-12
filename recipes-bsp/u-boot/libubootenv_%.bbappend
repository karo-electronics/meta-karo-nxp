# -----------------------------------------------------
# install fw_env.config
# -----------------------------------------------------
do_install:append () {
    if ${@ bb.utils.contains('DISTRO_FEATURES', 'u-boot-fw-utils', "true", "false", d)};then
        install -d ${D}${sysconfdir}
        install -m 644 ${DEPLOY_DIR_IMAGE}/u-boot/fw_env.config ${D}${sysconfdir}/fw_env.config
    fi
}
do_install[depends] += "u-boot:do_deploy"
