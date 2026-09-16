MACHINEOVERRIDES =. "mx8mn:"

require imx8m-base.inc

SOC_FAMILY = "imx8mn"
ATF_BL31_BASE ??= "0x960000"
IMX_WDOG_B_RESET = "0"
