PV = "2.10"
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}-${PV}:"

SRC_URI:append:mx9-nxp-bsp = " \
        file://patches/fmt-bugfix.patch \
        file://patches/errata-report-msg.patch \
        file://patches/get-console-from-fdt.patch \
"

SRC_URI:append = " \
        file://patches/bl31-addr-configurable.patch \
"

SRC_URI:append:mx9-nxp-bsp = " \
        file://patches/debug-verbosity.patch \
"
