package com.edgeburnmedia.batterystatusinfo.utils;

import com.edgeburnmedia.batterystatusinfo.BatteryStatus;
import com.edgeburnmedia.batterystatusinfo.client.BatteryStatusInfoModClient;
import net.minecraft.util.Identifier;
import oshi.SystemInfo;
import oshi.hardware.PowerSource;

/**
 * Utility class for getting battery information.
 */
public final class BatteryUtils {
	/**
	 * Instance of the OSHI SystemInfo class.
	 */
	private static final SystemInfo SYSTEM_INFO = new SystemInfo();

	private BatteryUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Get the battery percentage as a double between 0 and 1
	 *
	 * @return the battery percentage as a double between 0 and 1
	 */
	public static double getCharge() {
		return SYSTEM_INFO.getHardware().getPowerSources().get(0).getRemainingCapacityPercent();
	}

	/**
	 * Get whether the device is charging
	 *
	 * @return whether the device is charging
	 * @see PowerSource#isCharging()
	 */
	public static boolean isCharging() {
		return SYSTEM_INFO.getHardware().getPowerSources().get(0).isCharging();
	}

	/**
	 * Get the estimated time remaining on the battery in seconds
	 *
	 * @return the estimated time remaining on the battery in seconds
	 * @see PowerSource#getTimeRemainingEstimated()
	 */
	public static double getTimeRemaining() {
		return SYSTEM_INFO.getHardware().getPowerSources().get(0).getTimeRemainingEstimated();
	}

	/**
	 * @return the battery status as a String description for debugging purposes
	 */
	public static String getDebugInfo() {
		BatteryStatus status = BatteryStatusInfoModClient.batteryCheckerThread.getBatteryStatus();
		return "Battery percentage: " + status.getCharge() * 100 + "%, Charging: " + status.isCharging() + ", Time remaining: " + status.getTimeRemaining() + " seconds";
	}

	public static Identifier getBatteryIcon(BatteryStatus status) {
		return getBatteryIcon(status.getCharge(), status.isCharging());
	}

	public static Identifier getBatteryIcon(double charge, boolean charging) {
		if (charging) {
			if (isBetween(charge, 0, 0.25)) {
				return Icons.BATTERY_0_CHARGING;
			} else if (isBetween(charge, 0.25, 0.5)) {
				return Icons.BATTERY_25_CHARGING;
			} else if (isBetween(charge, 0.5, 0.75)) {
				return Icons.BATTERY_50_CHARGING;
			} else if (isBetween(charge, 0.75, 1)) {
				return Icons.BATTERY_75_CHARGING;
			} else if (charge == 1) {
				return Icons.BATTERY_FULL_CHARGING;
			} else {
				return Icons.BATTERY_UNKNOWN_CHARGING;
			}
		} else {
			if (isBetween(charge, 0, 0.25)) {
				return Icons.BATTERY_0;
			} else if (isBetween(charge, 0.25, 0.5)) {
				return Icons.BATTERY_25;
			} else if (isBetween(charge, 0.5, 0.75)) {
				return Icons.BATTERY_50;
			} else if (isBetween(charge, 0.75, 1)) {
				return Icons.BATTERY_75;
			} else if (charge == 1) {
				return Icons.BATTERY_FULL;
			} else {
				return Icons.BATTERY_UNKNOWN;
			}
		}
	}

	public static int getChargePercent(double charge) {
		return (int) Math.ceil(charge * 100);
	}

	private static boolean isBetween(double x, double a, double b) {
		return x >= a && x < b;
	}
}
