package com.edgeburnmedia.batterystatusinfo.gui;

import com.edgeburnmedia.batterystatusinfo.BatteryStatus;
import com.edgeburnmedia.batterystatusinfo.config.BatteryStatusInfoConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BatteryHud extends DrawContext {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	private static final int SCALE = 21;
	private final BatteryStatusInfoConfig config;

	public BatteryHud(BatteryStatusInfoConfig config) {
		this.config = config;
	}


	public void render(BatteryStatus status, MatrixStack matrices) {
		if (!config.isShowHud()) {
			return;
		}

		if (!config.isShowHudWhenFullyCharged() && status.getCharge() == 1) {
			return;
		}

		final int windowWidth = client.getWindow().getScaledWidth();
		final int windowHeight = client.getWindow().getScaledHeight();

		String text = Math.round(status.getCharge() * 100) + "%";

		BatteryStatusInfoConfig.Position position = config.getPosition();

		final Identifier texture = status.getBatteryIcon();
		matrices.push();
		client.getTextureManager().bindTexture(texture);
		RenderSystem.setShaderTexture(0, texture);

		int textWidth = client.textRenderer.getWidth(text);
		int textHeight = client.textRenderer.fontHeight;

		switch (position) {
			case TOP_LEFT -> {
				DrawContext.drawTexture(matrices, 1, 0, SCALE, SCALE, 0f, 0f, config.getHudIconScale(), config.getHudIconScale(), 16, 16);
				client.textRenderer.drawWithShadow(matrices, text, 23, 7, 0xFFFFFF);
			}
			case TOP_RIGHT -> {
				DrawContext.drawTexture(matrices, windowWidth - SCALE - 1, 0, SCALE, SCALE, 0f, 0f, config.getHudIconScale(), config.getHudIconScale(), 16, 16);
				client.textRenderer.drawWithShadow(matrices, text, windowWidth - textWidth - 23, 7, 0xFFFFFF);
			}
			case BOTTOM_LEFT -> {
				DrawContext.drawTexture(matrices, 1, windowHeight - SCALE - 1, SCALE, SCALE, 0f, 0f, config.getHudIconScale(), config.getHudIconScale(), 16, 16);
				client.textRenderer.drawWithShadow(matrices, text, 23, windowHeight - textHeight - 6, 0xFFFFFF);
			}
			case BOTTOM_RIGHT -> {
				DrawContext.drawTexture(matrices, windowWidth - SCALE - 1, windowHeight - SCALE - 1, SCALE, SCALE, 0f, 0f, config.getHudIconScale(), config.getHudIconScale(), 16, 16);
				client.textRenderer.drawWithShadow(matrices, text, windowWidth - textWidth - 23, windowHeight - textHeight - 6, 0xFFFFFF);
			}
		}

	}
}
