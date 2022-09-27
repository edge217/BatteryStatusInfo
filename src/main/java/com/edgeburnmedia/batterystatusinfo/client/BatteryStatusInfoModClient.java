package com.edgeburnmedia.batterystatusinfo.client;

import com.edgeburnmedia.batterystatusinfo.BatteryCheckerThread;
import com.edgeburnmedia.batterystatusinfo.BatteryMonitor;
import com.edgeburnmedia.batterystatusinfo.config.BatteryStatusInfoConfig;
import com.edgeburnmedia.batterystatusinfo.utils.BatteryUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class BatteryStatusInfoModClient implements ClientModInitializer {
	public static BatteryCheckerThread batteryCheckerThread;
	private static BatteryStatusInfoConfig config;
	private BatteryMonitor batteryMonitor;

	public static BatteryStatusInfoConfig getConfig() {
		return config;
	}

	@Override
	public void onInitializeClient() {
		AutoConfig.register(BatteryStatusInfoConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(BatteryStatusInfoConfig.class).getConfig();
		batteryCheckerThread = new BatteryCheckerThread();
		batteryCheckerThread.start();
		batteryMonitor = new BatteryMonitor();

		// Register debug command
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(ClientCommandManager.literal("lbadebug").executes(context -> {
				context.getSource().sendFeedback(Text.of(BatteryUtils.getDebugInfo()));
				return 0;
			}));
		});

		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			batteryCheckerThread.halt();
		});

		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			batteryMonitor.check(batteryCheckerThread.getBatteryStatus());
		});


	}
}
