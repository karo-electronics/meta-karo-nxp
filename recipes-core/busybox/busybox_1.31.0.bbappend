FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:${THISDIR}/files:${THISDIR}/patches:"

SRC_URI_remove = " \
                 file://ftpget.cfg \
                 file://login-utilities.cfg \
                 file://mdev.cfg \
                 file://resize.cfg \
                 ${@ bb.utils.contains('VIRTUAL-RUNTIME_dev_manager','busybox-mdev','','file://mdev.cfg',d)} \
                 file://sha1sum.cfg \
"
SRC_URI_append = " \
                 file://mdev \
                 file://rtcsymlink.sh \
                 ${@ bb.utils.contains('VIRTUAL-RUNTIME_dev_manager','busybox-mdev','','file://no-mdev.cfg',d)} \
                 ${@ bb.utils.contains('DISTRO_FEATURES','pam','file://pam.cfg','',d)} \
		 ${@ bb.utils.contains('DISTRO_FEATURES','busybox-crond','','file://no-crond.cfg',d)} \
"
FILES_${PN} += "${sysconfdir}/network/run"

# overrule prio 200 of sysvinit and shadow
# if enabled, /bin/sh will be linked to /bin/busybox.nosuid
# as workaround this is disabled, and will increase the size of the rootfs
#ALTERNATIVE_PRIORITY = "201"

#BUSYBOX_SPLIT_SUID = "0"
RDEPENDS_busybox-mdev += "bash"

#PROVIDES += "${PN}-inetd"
#PACKAGES =+ "${PN}-inetd"
FILES_${PN}-inetd = "${sysconfdir}/init.d/inetd.${PN}"
INITSCRIPT_NAME_${PN}-inetd = "inetd.${PN}"
#INITSCRIPT_PARAMS_${PN}-inetd = "start 02 2 ."
INITSCRIPT_PACKAGES += "${PN}-inetd"
DEPENDS_${PN}-inetd += "update-rc.d-native"

inherit useradd relative_symlinks update-rc.d

USERADD_PACKAGES += "${PN}"
USERADD_PARAM_${PN} = ""
GROUPADD_PARAM_${PN} = "--system utmp"

# need /etc/group in the staging directory
DEPENDS += "base-passwd"

FILES_${PN} += "/run/utmp ${localstatedir}/log/wtmp"

do_install_append_${PN}-inetd() {
    mv -v ${D}${sysconfdir}/init.d/inetd.${BPN} ${D}${sysconfdir}/init.d/inetd
    update-rc.d -r ${D} inetd start 02 2 .
}

do_install_append () {
    if ${@ bb.utils.contains('VIRTUAL-RUNTIME_dev_manager','busybox-mdev','true','false',d)};then
        echo "Using busybox-mdev as device manager"
    elif ${@ bb.utils.contains('VIRTUAL-RUNTIME_dev_manager','udev','true','false',d)};then
        echo "Using ${@ d.getVar('VIRTUAL-RUNTIME_dev_manager')} as device manager"
    else
        echo "ERROR: VIRTUAL-RUNTIME_dev_manager is not set!" >&2
        exit 1
    fi
    if grep "CONFIG_FEATURE_MDEV_CONF=y" ${B}/.config; then
        install -v -m 0755 ${WORKDIR}/rtcsymlink.sh ${D}${sysconfdir}/mdev
    fi

    install -d -m 0755 ${D}${sysconfdir}/network
    ln -snvf /run/network ${D}${sysconfdir}/network/run

    install -v -d -m 0755 ${D}/run
    install -v -m 0664 -g utmp /dev/null ${D}/run/utmp

    install -v -d -m 0755 ${D}${localstatedir}/log
    install -v -m 0664 -g utmp /dev/null ${D}${localstatedir}/log/wtmp
}
