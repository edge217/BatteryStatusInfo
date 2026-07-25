package com.edgeburnmedia.batterystatusinfo.gui;

import com.edgeburnmedia.batterystatusinfo.BatteryStatus;
import com.edgeburnmedia.batterystatusinfo.config.BatteryStatusInfoConfig;
import com.edgeburnmedia.batterystatusinfo.utils.BatteryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;

public class BatteryHud {
	private static final Minecraft client = Minecraft.getInstance();
	private static final int SCALE = 21;
	private static final int PERCENT_COLOUR = CommonColors.WHITE;
	private final BatteryStatusInfoConfig config;

	public BatteryHud(BatteryStatusInfoConfig config) {
		this.config = config;
	}

	public void render(BatteryStatus status, GuiGraphicsExtractor drawContext) {
		if (!config.isShowHud()) {
			return;
		}

		if (!config.isShowHudWhenFullyCharged() && status.isFullyCharged()) {
			return;
		}

		final int windowWidth = client.getWindow().getGuiScaledWidth();
		final int windowHeight = client.getWindow().getGuiScaledHeight();

		String text = BatteryUtils.getChargePercent(status.getCharge()) + "%";

		BatteryStatusInfoConfig.Position position = config.getPosition();

		final Identifier texture = status.getBatteryIcon();

		int textWidth = client.font.width(text);
		int textHeight = client.font.lineHeight;

        switch (position) {
			case TOP_LEFT -> {
				drawContext.blit(RenderPipelines.GUI_TEXTURED, texture, 1, 0, 0, 0, SCALE, SCALE, SCALE, SCALE);
				drawContext.text(client.font, text, 23, 7, PERCENT_COLOUR);
			}
			case TOP_RIGHT -> {
				drawContext.blit(RenderPipelines.GUI_TEXTURED, texture, windowWidth - SCALE - 1, 0, 0, 0, SCALE, SCALE, SCALE, SCALE);
				drawContext.text(client.font, text, windowWidth - textWidth - 23, 7, PERCENT_COLOUR);
			}
			case BOTTOM_LEFT -> {
				drawContext.blit(RenderPipelines.GUI_TEXTURED, texture, 1, windowHeight - SCALE - 1, 0, 0, SCALE, SCALE, SCALE, SCALE);
				drawContext.text(client.font, text, 23, windowHeight - textHeight - 6, PERCENT_COLOUR);
			}
			case BOTTOM_RIGHT -> {
				drawContext.blit(RenderPipelines.GUI_TEXTURED, texture, windowWidth - SCALE - 1, windowHeight - SCALE - 1, 0, 0, SCALE, SCALE, SCALE, SCALE);
				drawContext.text(client.font, text, windowWidth - textWidth - 23, windowHeight - textHeight - 6, PERCENT_COLOUR);
			}
		}

	}
}
