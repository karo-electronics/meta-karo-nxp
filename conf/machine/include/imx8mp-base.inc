MACHINEOVERRIDES =. "mx8mp:"

require imx8m-base.inc

SOC_FAMILY = "imx8mp"
ATF_BL31_BASE ??= "0x970000"
IMX_WDOG_B_RESET = "1"
