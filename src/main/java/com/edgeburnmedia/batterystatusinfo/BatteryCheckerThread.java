package com.edgeburnmedia.batterystatusinfo;

import com.edgeburnmedia.batterystatusinfo.client.BatteryStatusInfoModClient;
import com.edgeburnmedia.batterystatusinfo.utils.BatteryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.PowerSource;

import java.util.ArrayList;

/**
 * Background thread that checks the battery status every 5 seconds.
 *
 * @author Edgeburn Media
 */
public class BatteryCheckerThread extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(BatteryCheckerThread.class);
	private boolean running;
	private double batteryPercentage;
	private boolean charging;
	private double timeRemaining;

	/**
	 * Get the battery percentage as a double between 0 and 1
	 *
	 * @return the battery percentage
	 */
	public double getBatteryPercentage() {
		return batteryPercentage;
	}

	/**
	 * Get whether the device is charging
	 *
	 * @return whether the device is charging
	 */
	public boolean isCharging() {
		return charging;
	}

	public BatteryStatus getBatteryStatus() {
		return new BatteryStatus(batteryPercentage, charging, timeRemaining);
	}

	@Override
	public void run() {
		setName("Battery Checker Thread");
		running = true;
		long lastCheck = 0;
		LOGGER.info("Starting battery checker thread");
		while (running) {
			long time = System.currentTimeMillis();
			if (time - lastCheck >= BatteryStatusInfoModClient.getConfig().getCheckInterval()) {
				lastCheck = time;
				batteryPercentage = BatteryUtils.getCharge(getPowerSource());
				charging = BatteryUtils.isCharging(getPowerSource());
				timeRemaining = BatteryUtils.getTimeRemaining(getPowerSource());
			}
		}
		LOGGER.info("Battery checker thread stopped");
	}

	/**
	 * Stop the thread
	 */
	public void halt() {
		running = false;
	}

	public PowerSource getPowerSource() {
		SystemInfo SYSTEM_INFO = new SystemInfo();
		ArrayList<PowerSource> powerSources = new ArrayList<>(SYSTEM_INFO.getHardware().getPowerSources());
		if (powerSources.size() == 0) {
			throw new IllegalStateException("No power sources found");
		} else if (powerSources.size() == 1) {
			LOGGER.info("1 power source found: " + BatteryUtils.getPowerSourceDescription(powerSources.get(0)));
			return powerSources.get(0);
		} else {
			LOGGER.info(powerSources.size() + " power sources found...");
			for (PowerSource powerSource : powerSources) {
				LOGGER.info("Power source: " + BatteryUtils.getPowerSourceDescription(powerSource) + ", remaining capacity: " + powerSource.getRemainingCapacityPercent() + "%");
			}
		}
		throw new IllegalStateException("More than one power source found");
	}

}
