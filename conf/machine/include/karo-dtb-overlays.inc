def mach_overlay (d, overlay, mach="", dist=""):
    """
    Select device tree overlay name based on machine and distro constraints.

    Resolves overlay names that may contain machine-specific alternatives.
    An overlay with a colon (:) provides two variants: the part before ':' is
    used if the machine matches MACHINEOVERRIDES, otherwise the part after ':'.

    Args:
        d: BitBake data dictionary
        overlay: Overlay name, optionally with ':' separator for alternatives
        mach: Machine name to check (default: "", matches any)
        dist: Required distro feature (default: "", no check)

    Returns:
        str: Selected overlay name, or "" if constraints are not satisfied
    """
    if dist:
        if not dist in d.getVar('DISTRO_FEATURES'):
            return ""
    if overlay.find(":") >= 0:
        alt1,alt2 = overlay.split(':')
        if mach == "":
            bb.warn("Alternative overlays '%s' specified, but no machine name(s) provided" % overlay)
            return alt1
    else:
        alt1,alt2 = (overlay,'')
    for m in mach.split(','):
        if m in d.getVar('MACHINEOVERRIDES'):
            return alt1
    return alt2

def get_overlays(d):
    """
    Build space-separated list of device tree overlay file paths.

    Expands DTB_OVERLAYS_generic and DTB_OVERLAYS variables into full paths.
    Generic overlays use the pattern: [DTB_VENDOR_DIR/]SOC_PREFIX-name.DTB_OVERLAY_EXT
    Machine-specific overlays use: [DTB_VENDOR_DIR/]SOC_FAMILY-name.DTB_OVERLAY_EXT
    Handles comma-separated overlay names and removes duplicates.

    Args:
        d: BitBake data dictionary containing DTB-related configuration variables

    Returns:
        str: Space-separated list of overlay file paths, or "" if no overlays are defined
    """
    ovlist = []
    g = d.getVar('DTB_OVERLAYS_generic') or ""
    o = d.getVar('DTB_OVERLAYS') or ""
    for ov in g.split():
        for fn in ov.split(","):
            fn = " freescale/%s-%s.dtbo" % (d.getVar('SOC_PREFIX'), fn)
            if fn in ovlist:
                bb.debug(2, "'%s' is already in overlays" % fn)
            else:
                bb.debug(2, "Adding '%s' to overlays" % fn)
                ovlist += [fn]

    for ov in o.split():
        for fn in ov.split(","):
            fn = " freescale/%s-%s.dtbo" % (d.getVar('SOC_FAMILY'), fn)
            if fn in ovlist:
                bb.debug(2, "'%s' is already in overlays" % fn)
            else:
                bb.debug(2, "Adding '%s' to overlays" % fn)
                ovlist += [fn]

    if len(ovlist) == 0:
        return ""
    return " ".join(ovlist)
