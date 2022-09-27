package com.edgeburnmedia.batterystatusinfo.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Config(name = "batterystatusinfo")
public class BatteryStatusInfoConfig implements ConfigData {
	@ConfigEntry.Category("alerts")
	boolean showLowBatteryAlert = true;
	@ConfigEntry.Category("alerts")
	boolean showFullyChargedAlert = true;
	@ConfigEntry.Category("alerts")
	boolean showChargingAlert = true;
	@ConfigEntry.Category("alerts")
	@ConfigEntry.Gui.PrefixText
	boolean showDischargingAlert = true;
	@ConfigEntry.Category("general")
	@ConfigEntry.BoundedDiscrete(max = 99, min = 1)
	int lowBatteryThreshold = 20;
	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.Tooltip
	long checkInterval = 5000;

	public boolean isShowLowBatteryAlert() {
		return showLowBatteryAlert;
	}

	public boolean isShowFullyChargedAlert() {
		return showFullyChargedAlert;
	}

	public boolean isShowChargingAlert() {
		return showChargingAlert;
	}

	public boolean isShowDischargingAlert() {
		return showDischargingAlert;
	}

	public int getLowBatteryThreshold() {
		return lowBatteryThreshold;
	}

	public long getCheckInterval() {
		return checkInterval;
	}

}
