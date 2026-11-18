# meta-karo-nxp

BSP Layer for the Ka-Ro Yocto BSP for Ka-Ro modules with NXP i.MX
processors.

For documentation see our Yocto Guide: https://karo-electronics.github.io/docs/yocto-guide/nxp/index.html

## Machines

* `qs95-9610` - Ka-Ro QS95-9610 module (NXP i.MX95)

## Dependencies

* `openembedded-core` (layer series `wrynose`, Yocto Project 6.0)
* `meta-freescale` / `meta-imx`: `imx-base.inc` machine includes and the
  `fsl-bsp-release` collection (see `LAYERDEPENDS` in `conf/layer.conf`)
* used together with `meta-karo-distro`, which selects this layer via
  `LAYERDEPENDS` and the `use-nxp-bsp` override

## Layer configuration variables

Custom variables defined by this layer (see the referencing files for
details):

* `KARO_BASEBOARD` - baseboard variant of the module; also available as
  the built-in `baseboard` OE fragment (`conf/layer.conf`,
  `conf/machine/include/karo-base.inc`)
* `KARO_BASEBOARDS` - baseboards supported by a machine
  (`conf/machine/include/imx95-overlays.inc`)
* `KARO_DTB_OVERLAYS[<baseboard>]` - device tree overlays loaded by
  default on the given baseboard
* `DTB_OVERLAYS`, `DTB_OVERLAYS_generic` - additional device tree
  overlays appended to `KERNEL_DEVICETREE`
  (`conf/machine/include/karo-overlays.inc`)
* `NETWORK_INTERFACES` - network interfaces of the machine, set in
  `conf/machine/qs95-9610.conf`

## License

The layer metadata is released under the MIT license, see the LICENSE
file.

---------
(c) 2020 - Ka-Ro electronics GmbH - www.karo-electronics.com
