PV = "2.10"
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}-${PV}/patches:"

SRC_URI:append = " \
        file://get-console-from-fdt.patch \
        file://fmt-bugfix.patch \
        file://debug-verbosity.patch \
        file://errata-report-msg.patch \
"

EXTRA_OEMAKE:append = " \
        CRASH_REPORTING=1 \
        LOG_LEVEL=30 \
        DEBUG=0 \
"

EXTRA_OEMAKE:append:mx8-nxp-bsp = " \
        RESET_TO_BL31=1 \
        IMX_WDOG_B_RESET=${@ 1 if 'imx8mp' in d.getVar('MACHINEOVERRIDES').split(':') else 0} \
        IMX_BOOT_UART_BASE=${IMX_BOOT_UART_BASE} \
"
EXTRA_OEMAKE:append:mx8-nxp-bsp = " \
        ERRATA_A53_1530924=1 \
"

EXTRA_OEMAKE:append:mx9-nxp-bsp = " \
        IMX_LPUART_BASE=${IMX_BOOT_UART_BASE} \
"
EXTRA_OEMAKE:append:mx9-nxp-bsp = " \
        ERRATA_DSU_798953=1 \
        ERRATA_DSU_936184=1 \
        ERRATA_A55_768277=1 \
        ERRATA_A55_778703=1 \
        ERRATA_A55_798797=1 \
        ERRATA_A55_846532=1 \
        ERRATA_A55_903758=1 \
        ERRATA_A55_1221012=1 \
        ERRATA_A55_1530923=1 \
"
