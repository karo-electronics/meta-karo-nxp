# Copyright (C) 2012, 2015 O.S. Systems Software LTDA.
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "GPL-2.0-only"

inherit kernel fsl-vivante-kernel-driver-handler
LINUX_VERSION_EXTENSION ?= "-karo"

S = "${WORKDIR}/git"
