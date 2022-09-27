package com.edgeburnmedia.batterystatusinfo.gui;

import com.edgeburnmedia.batterystatusinfo.BatteryStatus;
import com.edgeburnmedia.batterystatusinfo.config.BatteryStatusInfoConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BatteryHud extends DrawableHelper {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	private final BatteryStatusInfoConfig config;

	public BatteryHud(BatteryStatusInfoConfig config) {
		this.config = config;
	}


	public void render(BatteryStatus status, MatrixStack matrices) {
		if (!config.isShowHud()) {
			return;
		}

		// TODO add check for config to not show when fully charged

		final int windowWidth = client.getWindow().getScaledWidth();
		final int windowHeight = client.getWindow().getScaledHeight();

		String text = Math.round(status.getCharge() * 100) + "%";

		BatteryStatusInfoConfig.Position position = config.getPosition();

		final Identifier texture = status.getBatteryIcon();
		matrices.push();
		client.getTextureManager().bindTexture(texture);
		RenderSystem.setShaderTexture(0, texture);

		switch (position) {
			case TOP_LEFT -> {
			}
			case TOP_RIGHT -> {
			}
			case BOTTOM_LEFT -> {
			}
			case BOTTOM_RIGHT -> {
				int y = windowHeight - 16;

				int textWidth = client.textRenderer.getWidth(text);
				int textHeight = client.textRenderer.fontHeight;
				int textX = windowWidth - textWidth - 4;
				DrawableHelper.drawTexture(matrices, textX - config.getHudIconScale() - 2, (int) (y - (textHeight / 4.0)), 0f, 0f, config.getHudIconScale(), config.getHudIconScale(), 16, 16);
				client.textRenderer.drawWithShadow(matrices, text, textX, y + 2, 0xFFFFFF);

			}
		}

	}
}
