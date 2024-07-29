package com.edgeburnmedia.batterystatusinfo.client;

import com.edgeburnmedia.batterystatusinfo.BatteryCheckerThread;
import com.edgeburnmedia.batterystatusinfo.BatteryMonitor;
import com.edgeburnmedia.batterystatusinfo.BatteryStatus;
import com.edgeburnmedia.batterystatusinfo.config.BatteryStatusInfoConfig;
import com.edgeburnmedia.batterystatusinfo.gui.BatteryHud;
import com.edgeburnmedia.batterystatusinfo.toast.BatteryAlertToast;
import com.edgeburnmedia.batterystatusinfo.utils.BatteryUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

@Environment(EnvType.CLIENT)
public class BatteryStatusInfoModClient implements ClientModInitializer {
	public static BatteryCheckerThread batteryCheckerThread;
	private static BatteryStatusInfoConfig config;
	private static boolean gameResourcesReady = false;
	private BatteryMonitor batteryMonitor;

	public BatteryMonitor getBatteryMonitor() {
		return batteryMonitor;
	}

	private BatteryHud batteryHud;

	public static BatteryStatusInfoConfig getConfig() {
		return config;
	}

	public static boolean isGameResourcesReady() {
		return gameResourcesReady;
	}

	/**
	 * Mark that the game resources are ready, so that text can be displayed
	 */
	public static void gameResourcesReady() {
		gameResourcesReady = true;
	}

	@Override
	public void onInitializeClient() {
		var configHolder = AutoConfig.register(BatteryStatusInfoConfig.class, GsonConfigSerializer::new);
		configHolder.registerSaveListener((configHolder2, config) -> {
			batteryCheckerThread.notifyConfigurationChanges();
			return ActionResult.PASS;
		});
		config = configHolder.getConfig();
		batteryCheckerThread = new BatteryCheckerThread();
		batteryCheckerThread.start();
		batteryMonitor = new BatteryMonitor();
		batteryHud = new BatteryHud(getConfig());

		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
			batteryHud.render(batteryCheckerThread.getBatteryStatus(), drawContext);
		});

		// Register debug command
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(ClientCommandManager.literal("bsidebug").executes(context -> {
				context.getSource().sendFeedback(Text.of(BatteryUtils.getDebugInfo()));
				return 0;
			}));
		});

		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("bsi_icons_debug")
					.then(argument("charge", DoubleArgumentType.doubleArg(0.0, 1.0))
							.then(argument("charging", BoolArgumentType.bool())
									.executes(context -> {
										double charge = DoubleArgumentType.getDouble(context, "charge");
										boolean charging = BoolArgumentType.getBool(context, "charging");
										BatteryStatus status = new BatteryStatus(charge, charging, 0);
										showDebugToast(status);
										return Command.SINGLE_SUCCESS;
									}))));


		}));
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			batteryCheckerThread.halt();
		});

		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			batteryMonitor.check(batteryCheckerThread.getBatteryStatus());
		});
	}

	private void showDebugToast(BatteryStatus status) {
        new BatteryAlertToast(status, (double) getConfig().getLowBatteryThreshold() / 100.0).show();
	}

}
