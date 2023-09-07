package com.edgeburnmedia.batterystatusinfo;

import com.edgeburnmedia.batterystatusinfo.client.BatteryStatusInfoModClient;
import com.edgeburnmedia.batterystatusinfo.config.BatteryStatusInfoConfig;
import com.edgeburnmedia.batterystatusinfo.toast.BatteryAlertToast;
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
				new BatteryAlertToast(status, getLowBatteryThreshold()).show();
				lowBatteryAlerted = true;
			}
		} else {
			lowBatteryAlerted = false;
		}

		if (status.getCharge() >= 1 && config.isShowFullyChargedAlert()) {
			if (!fullyChargedAlerted) {
				new BatteryAlertToast(status, getLowBatteryThreshold()).show();
				fullyChargedAlerted = true;
			}
		} else {
			fullyChargedAlerted = false;
		}

		if (status.isCharging() && config.isShowChargingAlert()) {
			if (!chargingAlerted) {
				new BatteryAlertToast(status, getLowBatteryThreshold()).show();
				chargingAlerted = true;
				dischargingAlerted = false;
			}
		} else {
			chargingAlerted = false;
		}

		if (!status.isCharging() && config.isShowDischargingAlert()) {
			if (!dischargingAlerted) {
				new BatteryAlertToast(status, getLowBatteryThreshold()).show();
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
