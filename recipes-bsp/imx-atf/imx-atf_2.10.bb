# Copyright (C) 2017-2024 NXP

DESCRIPTION = "i.MX ARM Trusted Firmware"
SECTION = "BSP"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

PV .= "+git${SRCPV}"

SRC_URI = "${ATF_SRC};branch=${SRCBRANCH}"
ATF_SRC ?= "git://github.com/nxp-imx/imx-atf.git;protocol=https"
SRCBRANCH = "lf_v2.10"
SRCREV = "28affcae957cb8194917b5246276630f9e6343e1"

S = "${WORKDIR}/git"

inherit deploy

ATF_PLATFORM ??= "INVALID"

EXTRA_OEMAKE:append = " \
    CROSS_COMPILE="${TARGET_PREFIX}" \
    PLAT=${ATF_PLATFORM} \
"

EXTRA_OEMAKE:append = " \
    CRASH_REPORTING=1 \
    LOG_LEVEL=${@ 50 if d.getVar('ATF_DEBUG') == "1" else 30} \
    V=0 \
"

EXTRA_OEMAKE:append:mx8-nxp-bsp = " \
    IMX_BOOT_UART_BASE=${IMX_BOOT_UART_BASE} \
    IMX_WDOG_B_RESET=${@ 1 if 'imx8mp' in d.getVar('MACHINEOVERRIDES').split(':') else 0} \
    RESET_TO_BL31=1 \
    ERRATA_A53_1530924=1 \
"

EXTRA_OEMAKE:append:mx9-nxp-bsp = " \
    IMX_LPUART_BASE=${IMX_BOOT_UART_BASE} \
    IMX_WDOG_B_RESET=1 \
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

EXTRA_OEMAKE:append = " \
    BL31_BASE=${ATF_BL31_BASE} \
    BL31_SIZE=${ATF_BL31_SIZE} \
    SAVED_DRAM_TIMING_BASE=${UBOOT_SAVED_DRAM_TIMING_BASE} \
"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

# Baremetal, just need a compiler
INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "virtual/${HOST_PREFIX}gcc"

# Bring in clang compiler if using clang as default
DEPENDS:append:toolchain-clang = " clang-cross-${TARGET_ARCH}"

BUILD_OPTEE = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'true', 'false', d)}"

# CC and LD introduce arguments which conflict with those otherwise provided by
# this recipe. The heads of these variables excluding those arguments
# are therefore used instead.
def remove_options_tail (in_string):
    from itertools import takewhile
    return ' '.join(takewhile(lambda x: not x.startswith('-'), in_string.split(' ')))

EXTRA_OEMAKE += 'LD="${@remove_options_tail(d.getVar('LD'))}"'

EXTRA_OEMAKE += 'CC="${@remove_options_tail(d.getVar('CC'))}"'

# Set to 1 for debugging
ATF_DEBUG ?= "0"
EXTRA_OEMAKE += 'DEBUG=${ATF_DEBUG}'

do_configure() {
    oe_runmake clean
}
do_configure[vardeps] += "ATF_BL31_BASE ATF_BL31_SIZE UBOOT_SAVED_DRAM_TIMING_BASE"

do_compile() {
    # 'make clean' before compiling because atf build system does not recognise parameter changes
    # Clear LDFLAGS to avoid the option -Wl recognize issue
    oe_runmake bl31
    if ${BUILD_OPTEE}; then
        oe_runmake clean BUILD_BASE=build-optee
        oe_runmake BUILD_BASE=build-optee SPD=opteed bl31
    fi
}

do_install[noexec] = "1"

BOOT_TOOLS = "imx-boot-tools"

addtask deploy after do_compile
do_deploy() {
    builddir="${@ "debug" if d.getVar('ATF_DEBUG') == "1" else "release"}"
    if ${BUILD_OPTEE}; then
        install -vDm 0644 ${S}/build-optee/${ATF_PLATFORM}/${builddir}/bl31.bin ${DEPLOYDIR}/bl31-${ATF_PLATFORM}.bin-optee
        install -vDm 0644 ${S}/build-optee/${ATF_PLATFORM}/${builddir}/bl31.bin ${DEPLOYDIR}/${BOOT_TOOLS}/bl31-${ATF_PLATFORM}.bin-optee
    else
    install -vDm 0644 ${S}/build/${ATF_PLATFORM}/${builddir}/bl31.bin ${DEPLOYDIR}/bl31-${ATF_PLATFORM}.bin
    install -vDm 0644 ${S}/build/${ATF_PLATFORM}/${builddir}/bl31.bin ${DEPLOYDIR}/${BOOT_TOOLS}/bl31-${ATF_PLATFORM}.bin
    fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx8-generic-bsp|mx9-generic-bsp)"
