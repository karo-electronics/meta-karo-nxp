#!/bin/bash
#
# i.MX Yocto Project Build Environment Setup Script
#
# Copyright (C) 2011-2016 Freescale Semiconductor
# Copyright 2017 NXP
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
#
# Added changes to support
#
# Ka-Ro i.MX8 COMs inside the NXP Yocto Project Build Environment
#
# Copyright (C) 2019 Oliver Wendt <OW@KARO-electronics.de>
# Copyright (C) 2020 Lothar Waßmann <LW@KARO-electronics.de>
# Copyright (C) 2020 Markus Bauer <MB@KARO-electronics.de>
#

. sources/meta-imx/tools/setup-utils.sh

CWD=`pwd`
BASENAME="karo-setup-release.sh"
PROGNAME="setup-environment"

exit_message () {
    echo "To return to this build environment later please run:"
    echo -e "\tsource setup-environment <build_dir>"
}

usage() {
    echo "Usage: source $BASENAME [-b <build-dir>] [-h]"

    echo "Optional parameters:
* [-b <build-dir>]: Build directory, where <build-dir> is a sensible name of a
                    directory to be created.
                    If unspecified script uses 'build' as output directory.
* [-h]: help
"
}

test_builddir () {
    if [ ! -e $BUILD_DIR/conf/local.conf ]; then
	echo -e "\n ERROR - No build directory is set yet. Run the 'setup-environment' script before running this script to create " $BUILD_DIR
	echo -e "\n"
	return 1
    fi
}

clean_up()
{

    unset CWD BUILD_DIR FSLDISTRO
    unset fsl_setup_help fsl_setup_error fsl_setup_flag
    unset usage clean_up
    unset ARM_DIR META_FSL_BSP_RELEASE
    exit_message clean_up
}

# get command line options
OLD_OPTIND=$OPTIND
unset FSLDISTRO

while getopts b:h: fsl_setup_flag; do
    case ${fsl_setup_flag} in
	b)
	    BUILD_DIR="$OPTARG"
	    echo "Build directory is: " $BUILD_DIR
	   ;;
	h)
	    fsl_setup_help='true'
	   ;;
	?)
	    fsl_setup_error='true'
	   ;;
    esac
done
shift $((OPTIND-1))
if [ $# -ne 0 ]; then
    fsl_setup_error=true
    echo -e "Invalid command line ending: '$@'"
fi
OPTIND=$OLD_OPTIND
if test $fsl_setup_help; then
    usage && clean_up && return 1
elif test $fsl_setup_error; then
    clean_up && return 1
fi


if [ -z "$DISTRO" ]; then
    if [ -z "$FSLDISTRO" ]; then
        FSLDISTRO='fsl-imx-xwayland'
    fi
    DISTRO="$FSLDISTRO"
else
    FSLDISTRO="$DISTRO"
fi

if [ -z "$BUILD_DIR" ]; then
    BUILD_DIR='build-karo-nxp'
fi

if [ -z "$MACHINE" ]; then
    echo "Setting to default machine to 'tx8m-1610'"
    MACHINE='tx8m-1610'
fi

case $MACHINE in
    tx8m*)
        case $DISTRO in
            *wayland)
                : ok
                ;;
            *)
                echo -e "\n ERROR - Only Wayland distros are supported for i.MX 8 or i.MX 8M"
                echo -e "\n"
                return 1
                ;;
        esac
        ;;
    *)
        : ok
        ;;
esac

# copy new EULA into community so setup uses latest i.MX EULA
cp sources/meta-imx/EULA.txt sources/meta-freescale/EULA

# Set up the basic yocto environment
if [ -z "$DISTRO" ]; then
   DISTRO=$FSLDISTRO MACHINE=$MACHINE . ./$PROGNAME $BUILD_DIR
else
   MACHINE=$MACHINE . ./$PROGNAME $BUILD_DIR
fi

# Set CWD to a value again as it's being unset by the external scripts calls
[ -z $CWD ] && CWD=$CURRENT_CWD

TCWD=$(pwd)
# Point to the current directory since the last command changed the directory to $BUILD_DIR
BUILD_DIR=.

if [ ! -e $BUILD_DIR/conf/< ]; then
    echo -e "\n ERROR - No build directory is set yet. Run the 'setup-environment' script before running this script to create " $BUILD_DIR
    echo -e "\n"
    return 1
fi

# On the first script run, backup the local.conf file
# Consecutive runs, it restores the backup and changes are appended on this one.
if [ ! -e $BUILD_DIR/conf/local.conf.org ]; then
    cp $BUILD_DIR/conf/local.conf $BUILD_DIR/conf/local.conf.org
else
    cp $BUILD_DIR/conf/local.conf.org $BUILD_DIR/conf/local.conf
fi

# Share the same sstate for all builds
echo "SSTATE_DIR ?= \"\${BSPDIR}/sstate-cache\"" >> $BUILD_DIR/conf/local.conf


if [ ! -e $BUILD_DIR/conf/bblayers.conf.org ]; then
    cp $BUILD_DIR/conf/bblayers.conf $BUILD_DIR/conf/bblayers.conf.org
else
    cp $BUILD_DIR/conf/bblayers.conf.org $BUILD_DIR/conf/bblayers.conf
fi


META_FSL_BSP_RELEASE="${CWD}/sources/meta-imx/meta-bsp"

echo "" >> $BUILD_DIR/conf/bblayers.conf
echo "# i.MX Yocto Project Release layers" >> $BUILD_DIR/conf/bblayers.conf
hook_in_layer meta-imx/meta-bsp
hook_in_layer meta-imx/meta-sdk
hook_in_layer meta-imx/meta-ml

echo "" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-browser\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-rust\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-openembedded/meta-gnome\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-openembedded/meta-networking\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-openembedded/meta-filesystems\"" >> $BUILD_DIR/conf/bblayers.conf

echo "BBLAYERS += \"\${BSPDIR}/sources/meta-qt5\"" >> $BUILD_DIR/conf/bblayers.conf

echo "BBLAYERS += \"\${BSPDIR}/sources/meta-karo-nxp \"" >> $BUILD_DIR/conf/bblayers.conf

if [ -d ../sources/meta-ivi ]; then
    echo -e "\n## Genivi layers" >> $BUILD_DIR/conf/bblayers.conf
    echo "BBLAYERS += \"\${BSPDIR}/sources/meta-gplv2\"" >> $BUILD_DIR/conf/bblayers.conf
    echo "BBLAYERS += \"\${BSPDIR}/sources/meta-ivi/meta-ivi\"" >> $BUILD_DIR/conf/bblayers.conf
    echo "BBLAYERS += \"\${BSPDIR}/sources/meta-ivi/meta-ivi-bsp\"" >> $BUILD_DIR/conf/bblayers.conf
    echo "BBLAYERS += \"\${BSPDIR}/sources/meta-ivi/meta-ivi-test\"" >> $BUILD_DIR/conf/bblayers.conf
fi

echo BSPDIR=$BSPDIR
echo BUILD_DIR=$BUILD_DIR

# Support integrating community meta-freescale instead of meta-fsl-arm
if [ -d ../sources/meta-freescale ]; then
    echo meta-freescale directory found
    # Change settings according to environment
    sed -e "s,meta-fsl-arm\s,meta-freescale ,g" -i conf/bblayers.conf
    sed -e "s,\$.BSPDIR./sources/meta-fsl-arm-extra\s,,g" -i conf/bblayers.conf
fi

cd $BUILD_DIR
clean_up
unset FSLDISTRO
