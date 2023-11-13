#
# Copyright (C) 2022 Lothar Waßmann <LW@KARO-electronics.de>
# based on meta-freescale/recipes-bsp/imx-mkimage/imx-boot_1.0.bb Copyright (C) 2017-2020 NXP

require imx-mkimage_git.inc

DESCRIPTION = "Generate Boot Loader for i.MX 8 device"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"
SECTION = "BSP"

inherit use-imx-security-controller-firmware

IMX_EXTRA_FIRMWARE      = "firmware-imx-8 imx-sc-firmware imx-seco"

DEPENDS += " \
    u-boot \
    ${IMX_EXTRA_FIRMWARE} \
    imx-atf \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os', '', d)} \
"
# xxd is a dependency of fspi_packer.sh
DEPENDS += "xxd-native"
DEPENDS += "u-boot-imx-tools-native"
BOOT_NAME = "imx-boot-karo"
PROVIDES = "${BOOT_NAME}"
PROVIDES += "imx-boot"

inherit deploy

# Add CFLAGS with native INCDIR & LIBDIR for imx-mkimage build
CFLAGS = "-O2 -Wall -std=c99 -I ${STAGING_INCDIR_NATIVE} -L ${STAGING_LIBDIR_NATIVE}"

# This package aggregates output deployed by other packages,
# so set the appropriate dependencies
do_compile[depends] += " \
    virtual/bootloader:do_deploy \
    ${@' '.join('%s:do_deploy' % r for r in '${IMX_EXTRA_FIRMWARE}'.split() )} \
    imx-atf:do_deploy \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os:do_deploy', '', d)} \
"

ATF_MACHINE_NAME ?= "bl31-${ATF_PLATFORM}.bin"
ATF_MACHINE_NAME:append = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', '-optee', '', d)}"

IMX_BOOT_SOC_TARGET ?= "INVALID"

DEPLOY_OPTEE = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'true', 'false', d)}"

IMXBOOT_TARGETS ?= \
    "${@bb.utils.contains('UBOOT_CONFIG', 'fspi', 'flash_flexspi', \
        bb.utils.contains('UBOOT_CONFIG', 'nand', 'flash_nand', \
                                                  'flash_multi_cores flash_dcd', d), d)}"

B = "${WORKDIR}/build"
BOOT_STAGING = "${B}/${IMX_BOOT_SOC_TARGET}"

do_configure[noexec] = "1"

do_compile() {
    bbnote i.MX 93 boot binary build

    ( cd "${S}"; git rev-parse --short HEAD ) > ${B}/head.hash

    # Copy TEE binary to SoC target folder to mkimage
    for type in ${UBOOT_CONFIG};do
        BOOT_CONFIG_MACHINE="${BOOT_NAME}-${MACHINE}.${UBOOT_SUFFIX}-${type}"

        install -v -D ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.${UBOOT_SUFFIX}-${type} ${BOOT_STAGING}/${type}/u-boot.bin
        install -v ${DEPLOY_DIR_IMAGE}/u-boot-spl-${MACHINE}.${UBOOT_SUFFIX}-${type} ${BOOT_STAGING}/${type}/u-boot-spl.bin
        cat ${BOOT_STAGING}/${type}/u-boot.bin ${B}/head.hash > ${BOOT_STAGING}/${type}/u-boot-hash.bin

        ln -snvf "../../../git/${IMX_BOOT_SOC_TARGET}/soc.mak" "${BOOT_STAGING}/${type}"

        install -v ${DEPLOY_DIR_IMAGE}/${SECO_FIRMWARE_NAME}             ${BOOT_STAGING}/${type}
        install -v ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${ATF_MACHINE_NAME} ${BOOT_STAGING}/${type}/bl31.bin

        for ddr_firmware in ${DDR_FIRMWARE_NAME}; do
            bbnote "Copy ddr_firmware: ${ddr_firmware} from ${DEPLOY_DIR_IMAGE} -> ${BOOT_STAGING}/${type}"
            install -v ${DEPLOY_DIR_IMAGE}/${ddr_firmware}               ${BOOT_STAGING}/${type}
        done

        if ${DEPLOY_OPTEE}; then
            install -v "${DEPLOY_DIR_IMAGE}/tee.bin" "${BOOT_STAGING}"
        fi

        for target in ${IMXBOOT_TARGETS}; do
            bbnote "building ${IMX_BOOT_SOC_TARGET} - ${target} in `pwd`"
            make -C "${S}" MKIMG=${B}/mkimage_imx8 O="${BOOT_STAGING}/${type}" SOC_DIR=../${IMX_BOOT_SOC_TARGET}/${type} SOC=${IMX_BOOT_SOC_TARGET} dtbs=${UBOOT_DTB_NAME} ${target}
            install -v "${BOOT_STAGING}/${type}/flash.bin" "${S}/${BOOT_CONFIG_MACHINE}-${target}"
        done
    done
}

do_install () {
    install -v -d ${D}/boot

    for type in ${UBOOT_CONFIG};do
        BOOT_CONFIG_MACHINE="${BOOT_NAME}-${MACHINE}.${UBOOT_SUFFIX}-${type}"
        for target in ${IMXBOOT_TARGETS}; do
            install -v ${S}/${BOOT_CONFIG_MACHINE}-${target} ${D}/boot/
        done
    done
}

deploy_imx93() {
    for ddr_firmware in ${DDR_FIRMWARE_NAME}; do
        install -v ${DEPLOY_DIR_IMAGE}/${ddr_firmware}  ${DEPLOYDIR}/${BOOT_TOOLS}
    done

    for t in ${UBOOT_CONFIG};do
        install -v -d ${DEPLOYDIR}/${BOOT_TOOLS}/${t}
        install -v ${BOOT_STAGING}/${t}/${SECO_FIRMWARE_NAME}    ${DEPLOYDIR}/${BOOT_TOOLS}/${t}
        install -v ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${t} ${DEPLOYDIR}/${BOOT_TOOLS}/${t}
    done
}

do_deploy() {
    install -v -d ${DEPLOYDIR}/${BOOT_TOOLS}

    # copy tee.bin to deploy path
    if "${DEPLOY_OPTEE}"; then
        install -v ${DEPLOY_DIR_IMAGE}/tee.bin ${DEPLOYDIR}/${BOOT_TOOLS}
    fi

    deploy_${SOC_PREFIX}
    for type in ${UBOOT_CONFIG};do
        UBOOT_NAME="u-boot-${MACHINE}.${UBOOT_SUFFIX}-${type}"
        BOOT_CONFIG_MACHINE="${BOOT_NAME}-${MACHINE}.${UBOOT_SUFFIX}-${type}"

        install -v ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME} ${DEPLOYDIR}/${BOOT_TOOLS}

        # copy the generated boot image to deploy path
        for target in ${IMXBOOT_TARGETS}; do
            install -v ${S}/${BOOT_CONFIG_MACHINE}-${target} ${DEPLOYDIR}/${BOOT_NAME}-${MACHINE}-${type}.${UBOOT_SUFFIX}
            ln -snvf ${BOOT_NAME}-${MACHINE}-${type}.${UBOOT_SUFFIX} ${DEPLOYDIR}/${BOOT_NAME}-${type}
            ln -snvf ${BOOT_NAME}-${MACHINE}-${type}.${UBOOT_SUFFIX} ${DEPLOYDIR}/${BOOT_CONFIG_MACHINE}-${target}

            # Use first "target" as IMAGE_IMXBOOT_TARGET
            if [ "$IMAGE_IMXBOOT_TARGET" = "" ]; then
                IMAGE_IMXBOOT_TARGET="$target"
                bbnote "Set boot target as $IMAGE_IMXBOOT_TARGET"
                ln -snvf ${BOOT_NAME}-${type} ${DEPLOYDIR}/${BOOT_NAME}
            fi
        done
    done
}
addtask deploy before do_build after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES:${PN} = "/boot"

COMPATIBLE_MACHINE = "(mx9)"
