# Silence the boot-time ALSA errors on the i.MX6 modules.

FILESEXTRAPATHS:prepend:mx6-nxp-bsp := "${THISDIR}/${PN}:"

# No Ka-Ro card has a UCM profile; skip the lookup.
do_install:append:mx6-nxp-bsp() {
    if [ -f ${D}${sysconfdir}/init.d/alsa-state ]; then
        sed -i -e 's|alsactl -f \(.*\) restore|alsactl -U -f \1 restore|' \
            ${D}${sysconfdir}/init.d/alsa-state
    fi
}

# Same option for a postinst deferred to the first boot.
pkg_postinst:${PN}:mx6-nxp-bsp() {
	if test -z "$D"
	then
		if test -x ${sbindir}/alsactl
		then
			${sbindir}/alsactl -U -g -f ${localstatedir}/lib/alsa/asound.state restore
		fi
	fi
}
