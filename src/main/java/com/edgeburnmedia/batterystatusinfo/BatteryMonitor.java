package com.edgeburnmedia.batterystatusinfo;

import com.edgeburnmedia.batterystatusinfo.client.BatteryStatusInfoModClient;
import com.edgeburnmedia.batterystatusinfo.utils.BatteryUtils;
import com.edgeburnmedia.batterystatusinfo.utils.Icons;
import dev.cbyrne.toasts.impl.builder.BasicToastBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatteryMonitor {
	private static final Logger LOGGER = LoggerFactory.getLogger(BatteryMonitor.class);
	private boolean lowBatteryAlerted = false;
	private boolean fullyChargedAlerted = false;
	private boolean chargingAlerted = false;
	private boolean dischargingAlerted = true;
	private double lowBatteryThreshold;

	public BatteryMonitor() {
		lowBatteryThreshold = (double) BatteryStatusInfoModClient.getConfig().getLowBatteryThreshold() / 100.0; // FIXME make it so that when the config changes, the threshold is updated
	}

	public void check(BatteryStatus status) {
		if (status == null) {
			LOGGER.warn("Failed to check battery status!");
			return;
		}

		if (status.getCharge() <= lowBatteryThreshold) {
			if (!lowBatteryAlerted) {
				new BasicToastBuilder().title("Low Battery").description("Battery is at " + BatteryUtils.getChargePercent(status.getCharge()) + "%").icon(Icons.BATTERY_0).build().show();
				lowBatteryAlerted = true;
			}
		} else {
			lowBatteryAlerted = false;
		}

		if (status.getCharge() >= 1) {
			if (!fullyChargedAlerted) {
				new BasicToastBuilder().title("Fully Charged").description("Battery is at 100%").icon(Icons.BATTERY_FULL).build().show();
				fullyChargedAlerted = true;
			}
		} else {
			fullyChargedAlerted = false;
		}

		if (status.isCharging()) {
			if (!chargingAlerted) {
				new BasicToastBuilder().title("Battery Charging").description("Battery is at " + BatteryUtils.getChargePercent(status.getCharge()) + "%").icon(BatteryUtils.getBatteryIcon(status.getCharge(), true)).build().show();
				chargingAlerted = true;
				dischargingAlerted = false;
			}
		} else {
			chargingAlerted = false;
		}

		if (!status.isCharging()) {
			if (!dischargingAlerted) {
				new BasicToastBuilder().title("Charging Stopped").description("Battery is at " + BatteryUtils.getChargePercent(status.getCharge()) + "%").icon(BatteryUtils.getBatteryIcon(status.getCharge(), false)).build().show();
				dischargingAlerted = true;
			}
		} else {
			dischargingAlerted = false;
		}
	}

	public double getLowBatteryThreshold() {
		return lowBatteryThreshold;
	}

	public void setLowBatteryThreshold(double lowBatteryThreshold) {
		this.lowBatteryThreshold = lowBatteryThreshold;
	}
}
