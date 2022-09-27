package com.edgeburnmedia.batterystatusinfo;

import com.edgeburnmedia.batterystatusinfo.utils.BatteryUtils;
import net.minecraft.util.Identifier;

/**
 * Class representing the status of the battery at a given point.
 *
 * @author Edgeburn Media
 */
public class BatteryStatus {
	private final double charge;
	private final boolean isCharging;
	private final double timeRemaining;

	public BatteryStatus(double charge, boolean isCharging, double timeRemaining) {
		this.timeRemaining = timeRemaining;
		if (charge < 0 || charge > 1) {
			throw new IllegalArgumentException("Battery percentage must be between 0 and 1");
		}
		this.charge = charge;
		this.isCharging = isCharging;
	}

	public double getCharge() {
		return charge;
	}

	public boolean isCharging() {
		return isCharging;
	}

	public double getTimeRemaining() {
		return timeRemaining;
	}

	public Identifier getBatteryIcon() {
		return BatteryUtils.getBatteryIcon(this);
	}
}
