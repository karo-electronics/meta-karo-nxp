FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-karo:"
SRC_URI_append = " \
	file://karo.bmp;subdir=git/tools/logos \
	file://0001-Fix-alignment-of-reserved-memory-section.patch \
	file://0002-serial-Add-missing-dependencies-for-IMX8-to-MXC_UART.patch \
	file://0003-net-Add-missing-dependencies-for-IMX8-to-FEC_MXC.patch \
	file://0004-net-fec_mxc-add-missing-FEC_QUIRK_ENET_MAC-definitio.patch \
	file://0005-imx8m-add-missing-prototype-for-imx_get_mac_from_fus.patch \
	file://0006-net-define-missing-Kconfig-symbol-MII-which-is-selec.patch \
	file://0007-imx-spl-remove-weak-attribute-from-jump_to_image_no_.patch \
	file://0008-pmic-bd71837-silence-noisy-output-and-prevent-compil.patch \
	file://0009-usb-gadget-sdp-silence-noisy-messages.patch \
	file://0010-autoboot-don-t-change-bootcmd-to-fastboot-when-CMD_F.patch \
	file://0011-imx8mm-distinguish-between-watchdog-and-softreset.patch \
	file://0012-imx8mm-don-t-clear-the-reset-status-upon-reading-it.patch \
	file://0013-net-bootp-convert-messages-about-unhandled-DHCP-opti.patch \
	file://0014-common-autoboot-make-Normal-Boot-a-debug-message.patch \
	file://0015-fs-fs.c-correctly-interpret-the-max-len-parameter-to.patch \
	file://0016-drivers-ddr-imx8m-silence-noisy-messages.patch \
	file://0017-common-add-call-to-show_activity-in-main-cmd-loop.patch \
	file://0018-imx8mn-soc-put-env_get_location-in-board-specific-co.patch \
	file://0019-imx-make-use-of-WDOG_B-output-selectable-via-Kconfig.patch \
	file://0020-net-phy-micrel-cleanup-skew-config-code.patch \
	file://0021-net-phy-micrel-fix-the-max-value-that-is-used-to-cap.patch \
	file://0022-net-phy-micrel-support-storing-the-skew-parameters-i.patch \
	file://0023-env-fix-handling-of-.callbacks-variable.patch \
	file://0024-Ka-Ro-TX8M-QS8M-Support.patch \
"
