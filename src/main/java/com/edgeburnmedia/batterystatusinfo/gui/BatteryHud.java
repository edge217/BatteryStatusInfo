package com.edgeburnmedia.batterystatusinfo.gui;

import com.edgeburnmedia.batterystatusinfo.BatteryStatus;
import com.edgeburnmedia.batterystatusinfo.config.BatteryStatusInfoConfig;
import com.edgeburnmedia.batterystatusinfo.utils.BatteryUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class BatteryHud {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	private static final int SCALE = 21;
	private static final int PERCENT_COLOUR = 0xFFFFFF;
	private final BatteryStatusInfoConfig config;

	public BatteryHud(BatteryStatusInfoConfig config) {
		this.config = config;
	}

	public void render(BatteryStatus status, DrawContext drawContext) {
		if (!config.isShowHud()) {
			return;
		}

		if (!config.isShowHudWhenFullyCharged() && status.isFullyCharged()) {
			return;
		}

		final int windowWidth = client.getWindow().getScaledWidth();
		final int windowHeight = client.getWindow().getScaledHeight();

		String text = BatteryUtils.getChargePercent(status.getCharge()) + "%";

		BatteryStatusInfoConfig.Position position = config.getPosition();

		final Identifier texture = status.getBatteryIcon();

		int textWidth = client.textRenderer.getWidth(text);
		int textHeight = client.textRenderer.fontHeight;

        switch (position) {
			case TOP_LEFT -> {
				drawContext.drawTexture(texture, 1, 0, 0, 0, SCALE, SCALE, SCALE, SCALE);
				drawContext.drawTextWithShadow(client.textRenderer, text, 23, 7, PERCENT_COLOUR);
			}
			case TOP_RIGHT -> {
				drawContext.drawTexture(texture, windowWidth - SCALE - 1, 0, 0, 0, SCALE, SCALE, SCALE, SCALE);
				drawContext.drawTextWithShadow(client.textRenderer, text, windowWidth - textWidth - 23, 7, PERCENT_COLOUR);
			}
			case BOTTOM_LEFT -> {
				drawContext.drawTexture(texture, 1, windowHeight - SCALE - 1, 0, 0, SCALE, SCALE, SCALE, SCALE);
				drawContext.drawTextWithShadow(client.textRenderer, text, 23, windowHeight - textHeight - 6, PERCENT_COLOUR);
			}
			case BOTTOM_RIGHT -> {
				drawContext.drawTexture(texture, windowWidth - SCALE - 1, windowHeight - SCALE - 1, 0, 0, SCALE, SCALE, SCALE, SCALE);
				drawContext.drawTextWithShadow(client.textRenderer, text, windowWidth - textWidth - 23, windowHeight - textHeight - 6, PERCENT_COLOUR);
			}
		}

	}
}
