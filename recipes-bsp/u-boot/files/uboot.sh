echo "---------------  HELLO FROM Boot SCRIPT !!!!!!!!!!!!! -----------------"

test -n "${BOOT_ORDER}" || setenv BOOT_ORDER "A B"
test -n "${BOOT_A_LEFT}" || setenv BOOT_A_LEFT 3
test -n "${BOOT_B_LEFT}" || setenv BOOT_B_LEFT 3


#setenv bootargs_mmc_rauc
setenv slot_changed 0
setenv rauc_bootargs
for BOOT_SLOT in "${BOOT_ORDER}"; do
  if test "x${rauc_bootargs}" != "x";
  then
    echo "skip remaining slots ..."
  elif test "${BOOT_SLOT}" = "A";
  then
    echo "found BOOT_A_LEFT = ${BOOT_A_LEFT}"
    if test 0x${BOOT_A_LEFT} -gt 0;
    then
      echo "Found valid slot A, ${BOOT_A_LEFT} attempts remaining"
      setenv rauc_bootargs 'run default_bootargs;setenv bootargs ${bootargs} root=PARTUUID=${uuid_rootfs} rauc.slot=A rootwait ${append_bootargs} ${dyndbg}'
      if test $mmcpart != 1
      then
	echo "Slot change from B to A !"
	# we will do a reset of u-boot to reload devicetree and overlays properly
	setenv slot_changed 1
      else
	# only reduce the boot tries if we do not have a slot change
	setexpr BOOT_A_LEFT ${BOOT_A_LEFT} - 1
      fi
      setenv mmcpart 1
    fi
  elif test "${BOOT_SLOT}" = "B";
  then
    echo "found BOOT_B_LEFT = ${BOOT_B_LEFT}"
    if test 0x${BOOT_B_LEFT} -gt 0;
    then
      echo "Found valid slot B, ${BOOT_B_LEFT} attempts remaining"
      setenv rauc_bootargs 'run default_bootargs;setenv bootargs ${bootargs} root=PARTUUID=${uuid_rootfsB} rauc.slot=B rootwait ${append_bootargs} ${dyndbg}'
      if test $mmcpart != 4
      then
	echo "Slot change from A to B !"
	# we will do a reset of u-boot to reload devicetree and overlays properly
	setenv slot_changed 1
      else
	setexpr BOOT_B_LEFT ${BOOT_B_LEFT} - 1
      fi
      setenv mmcpart 4
    fi
  fi
done

if test -n "${rauc_bootargs}"; then
  echo "saving environment with BOOT_A_LEFT = ${BOOT_A_LEFT}    BOOT_B_LEFT = ${BOOT_B_LEFT}"
  if test $slot_changed = 1
  then
    setenv slot_changed
    saveenv
    reset
  else
    setenv slot_changed
    saveenv
  fi
else
  echo "No valid slot found, resetting tries to 3"
  setenv BOOT_A_LEFT 3
  setenv BOOT_B_LEFT 3
  saveenv
  reset
fi
