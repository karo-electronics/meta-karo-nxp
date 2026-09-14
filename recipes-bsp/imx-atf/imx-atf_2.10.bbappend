PV = "2.10"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BP}/patches:"

SRC_URI:append = " \
    file://errata-report-msg.patch \
    file://get-console-from-fdt.patch \
    file://bl31-addr-configurable.patch \
    file://debug-verbosity.patch \
"
