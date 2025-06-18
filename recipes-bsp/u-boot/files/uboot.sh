echo "---------------  HELLO FROM NEW SCRIPT !!!!!!!!!!!!! -----------------"

test -n "${BOOT_ORDER}" || setenv BOOT_ORDER "A B"
test -n "${BOOT_A_LEFT}" || setenv BOOT_A_LEFT 3
test -n "${BOOT_B_LEFT}" || setenv BOOT_B_LEFT 3


#setenv bootargs_mmc_rauc
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
      setexpr BOOT_A_LEFT ${BOOT_A_LEFT} - 1
      setenv rauc_bootargs 'run default_bootargs;setenv bootargs ${bootargs} root=PARTUUID=${uuid_rootfs} rauc.slot=A rootwait ${append_bootargs} ${dyndbg}'
      setenv mmcpart 1
    fi
  elif test "${BOOT_SLOT}" = "B";
  then
    echo "found BOOT_B_LEFT = ${BOOT_B_LEFT}"
    if test 0x${BOOT_B_LEFT} -gt 0;
    then
      echo "Found valid slot B, ${BOOT_B_LEFT} attempts remaining"
      setexpr BOOT_B_LEFT ${BOOT_B_LEFT} - 1
      setenv rauc_bootargs 'run default_bootargs;setenv bootargs ${bootargs} root=PARTUUID=${uuid_rootfsB} rauc.slot=B rootwait ${append_bootargs} ${dyndbg}'
      setenv mmcpart 4
    fi
  fi
done

if test -n "${rauc_bootargs}"; then
  echo "saving environment with BOOT_A_LEFT = ${BOOT_A_LEFT}    BOOT_B_LEFT = ${BOOT_B_LEFT}"
  saveenv
else
  echo "No valid slot found, resetting tries to 3"
  setenv BOOT_A_LEFT 3
  setenv BOOT_B_LEFT 3
  saveenv
  reset
fi

#echo "Loading kernel"
#run load_kernel_${boot_mode}
#echo " Starting kernel"
#bootm ${loadaddr_kernel}
