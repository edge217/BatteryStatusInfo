package com.edgeburnmedia.batterystatusinfo;

import com.edgeburnmedia.batterystatusinfo.client.BatteryStatusInfoModClient;
import com.edgeburnmedia.batterystatusinfo.utils.BatteryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Background thread that checks the battery status every 5 seconds.
 *
 * @author Edgeburn Media
 */
public class BatteryCheckerThread {
	private static final Logger LOGGER = LoggerFactory.getLogger(BatteryCheckerThread.class);
	private final Thread thread = new Thread(this::run);
	private volatile boolean running;
	private long lastCheck;
	private double batteryPercentage;
	private boolean charging;
	private double timeRemaining;

	{
		thread.setName("Battery Checker Thread");
		thread.setDaemon(true);
	}

	public BatteryStatus getBatteryStatus() {
		return new BatteryStatus(batteryPercentage, charging, timeRemaining);
	}

	public void run() {
		running = true;
		LOGGER.info("Starting battery checker thread");

		performBatteryCheck();

		while (running) {
			long time = System.currentTimeMillis();
			long timeToWait = lastCheck + BatteryStatusInfoModClient.getConfig().getCheckInterval() - time;
			try {
				if (timeToWait > 0) {
					//noinspection BusyWait // Not a busy wait, it is polling hardware value changes
					Thread.sleep(timeToWait);
				}
			} catch (InterruptedException e) {
				// this just needs to restart the loop for us
				continue;
			}

			performBatteryCheck();
		}
		LOGGER.info("Battery checker thread stopped");
	}

	private void performBatteryCheck() {
		lastCheck = System.currentTimeMillis();
		batteryPercentage = BatteryUtils.getCharge();
		charging = BatteryUtils.isCharging();
		timeRemaining = BatteryUtils.getTimeRemaining();
	}

	/**
	 * Start the thread
	 */
	public void start() {
		thread.start();
	}

	/**
	 * Stop the thread
	 */
	public void halt() {
		running = false;
		thread.interrupt();
	}

	/**
	 * Notify of configuration changes
	 */
	public void notifyConfigurationChanges() {
		thread.interrupt();
	}
}
