# -----------------------------------------------------
# install fw_env.config
# -----------------------------------------------------
do_install:append () {
    if [ -e ${DEPLOY_DIR_IMAGE}/fw_env.config ] ; then
        install -d ${D}${sysconfdir}
        install -m 644 ${DEPLOY_DIR_IMAGE}/fw_env.config ${D}${sysconfdir}/fw_env.config
    fi
}
