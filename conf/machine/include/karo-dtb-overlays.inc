#
# get overlay name depending on MACHINEOVERRIDES and DISTRO_FEATURES
#
def mach_overlay (d, overlay, mach="", dist=""):
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
