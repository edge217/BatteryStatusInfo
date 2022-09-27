package com.edgeburnmedia.batterystatusinfo;

import com.edgeburnmedia.batterystatusinfo.client.BatteryStatusInfoModClient;
import com.edgeburnmedia.batterystatusinfo.config.BatteryStatusInfoConfig;
import com.edgeburnmedia.batterystatusinfo.utils.BatteryUtils;
import com.edgeburnmedia.batterystatusinfo.utils.Icons;
import dev.cbyrne.toasts.impl.builder.BasicToastBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatteryMonitor {
	private static final Logger LOGGER = LoggerFactory.getLogger(BatteryMonitor.class);
	private final BatteryStatusInfoConfig config;
	private boolean lowBatteryAlerted = false;
	private boolean fullyChargedAlerted = false;
	private boolean chargingAlerted = false;
	private boolean dischargingAlerted = true;

	public BatteryMonitor() {
		config = BatteryStatusInfoModClient.getConfig();
	}

	public void check(BatteryStatus status) {
		if (status == null) {
			LOGGER.warn("Failed to check battery status!");
			return;
		}

		/*
		 * We don't want to show the toasts if the game resources aren't ready, because text will be rendered as boxes
		 */
		if (!BatteryStatusInfoModClient.isGameResourcesReady()) {
			return;
		}

		if (status.getCharge() <= getLowBatteryThreshold() && config.isShowLowBatteryAlert()) {
			if (!lowBatteryAlerted) {
				new BasicToastBuilder().title("Low Battery").description("Battery is at " + BatteryUtils.getChargePercent(status.getCharge()) + "%").icon(Icons.BATTERY_0).build().show();
				lowBatteryAlerted = true;
			}
		} else {
			lowBatteryAlerted = false;
		}

		if (status.getCharge() >= 1 && config.isShowFullyChargedAlert()) {
			if (!fullyChargedAlerted) {
				new BasicToastBuilder().title("Fully Charged").description("Battery is at 100%").icon(Icons.BATTERY_FULL).build().show();
				fullyChargedAlerted = true;
			}
		} else {
			fullyChargedAlerted = false;
		}

		if (status.isCharging() && config.isShowChargingAlert()) {
			if (!chargingAlerted) {
				new BasicToastBuilder().title("Battery Charging").description("Battery is at " + BatteryUtils.getChargePercent(status.getCharge()) + "%").icon(BatteryUtils.getBatteryIcon(status.getCharge(), true)).build().show();
				chargingAlerted = true;
				dischargingAlerted = false;
			}
		} else {
			chargingAlerted = false;
		}

		if (!status.isCharging() && config.isShowDischargingAlert()) {
			if (!dischargingAlerted) {
				new BasicToastBuilder().title("Charging Stopped").description("Battery is at " + BatteryUtils.getChargePercent(status.getCharge()) + "%").icon(BatteryUtils.getBatteryIcon(status.getCharge(), false)).build().show();
				dischargingAlerted = true;
			}
		} else {
			dischargingAlerted = false;
		}
	}

	public double getLowBatteryThreshold() {
		return (double) config.getLowBatteryThreshold() / 100.0;
	}

}
