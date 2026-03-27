PV = "2.10"
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}-${PV}:"

SRC_URI:append = " \
        file://patches/errata-report-msg.patch \
        file://patches/get-console-from-fdt.patch \
        file://patches/bl31-addr-configurable.patch \
        file://patches/debug-verbosity.patch \
"
