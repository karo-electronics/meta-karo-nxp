FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-karo:"
SRC_URI_append = " \
		file://karo.bmp;subdir=git/tools/logos \
		file://0001-Silence-warning-ignoring-attempt-to-redefine-built-i.patch \
		file://0002-Fix-alignment-of-reserved-memory-section.patch \
		file://0003-serial-Add-missing-dependencies-for-IMX8-to-MXC_UART.patch \
		file://0004-net-Add-missing-dependencies-for-IMX8-to-FEC_MXC.patch \
		file://0005-net-fec_mxc-add-missing-FEC_QUIRK_ENET_MAC-definitio.patch \
		file://0006-imx8m-add-missing-prototype-for-imx_get_mac_from_fus.patch \
		file://0007-net-define-missing-Kconfig-symbol-MII-which-is-selec.patch \
		file://0008-imx-spl-remove-weak-attribute-from-jump_to_image_no_.patch \
		file://0009-pmic-bd71837-silence-noisy-output-and-prevent-compil.patch \
		file://0010-usb-gadget-sdp-silence-noisy-messages.patch \
		file://0011-autoboot-don-t-change-bootcmd-to-fastboot-when-CMD_F.patch \
		file://0012-imx8m-use-WDOG_SRS-to-initiate-reset-rather-than-wai.patch \
		file://0013-imx8mm-distinguish-between-watchdog-and-softreset.patch \
		file://0014-imx8mm-don-t-clear-the-reset-status-upon-reading-it.patch \
		file://0015-net-bootp-convert-messages-about-unhandled-DHCP-opti.patch \
		file://0016-common-autoboot-make-Normal-Boot-a-debug-message.patch \
		file://0017-fs-fs.c-correctly-interpret-the-max-len-parameter-to.patch \
		file://0018-arm-imx-add-Ka-Ro-TX8M-1610-support.patch \
		file://0019-board-karo-tx8m-add-support-for-_noenv-U-Boot.patch \
		file://0020-board-karo-tx8m-add-support-for-_mfg-U-Boot.patch \
		file://0021-board-karo-tx8m-add-display-support-for-MIPI-DSI-bas.patch \
		file://0022-common-add-call-to-show_activity-in-main-cmd-loop.patch \
		file://0023-tx8mm-removed-loadfdt-from-bootcmd.patch "
