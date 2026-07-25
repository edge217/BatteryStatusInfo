/*
 * Copyright (c) 2023-2024 Edgeburn Media. All rights reserved.
 */

package com.edgeburnmedia.batterystatusinfo.toast;

import com.edgeburnmedia.batterystatusinfo.BatteryStatus;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;

public class BatteryAlertToast implements Toast {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "toast/advancement");
    private static final double DISPLAY_TIME = 3000;
    private static final int WHITE_COLOUR = CommonColors.WHITE;
    private static final int GRAY_COLOUR = CommonColors.LIGHT_GRAY;
    private final Identifier iconTexture;
    private final BatteryStatus status;
    private double lowBatteryThreshold;
    private long startTime;
    private boolean justUpdated = true;
    private Visibility visibility = Visibility.HIDE;

    public BatteryAlertToast(BatteryStatus status, double lowBatteryThreshold) {
        this.iconTexture = status.getBatteryIcon();
        this.status = status;
        this.lowBatteryThreshold = lowBatteryThreshold;
    }

    @Override
    public void update(ToastManager manager, long time) {
        if (this.justUpdated) {
            this.startTime = time;
            this.justUpdated = false;
        }

        this.visibility = time - this.startTime < DISPLAY_TIME ? Visibility.SHOW : Visibility.HIDE;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, Font textRenderer, long startTime) {
        context.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, 0, 1, 160, 32);

        context.blit(RenderPipelines.GUI_TEXTURED, iconTexture, 4,5,0,0, 21,21, 21, 21);
        // 4 + 21 + 2 = x of icon texture + width of icon texture + buffer space
        context.text(Minecraft.getInstance().font, getTitle(), 4+21+2,7, WHITE_COLOUR, false);

        context.text(textRenderer, getSub(), 4+21+2, 7+textRenderer.lineHeight, GRAY_COLOUR, false);
    }

    @Override
    public Visibility getWantedVisibility() {
        return visibility;
    }

    protected Component getSub() {
        return Component.translatableWithFallback("toast.batterystatusinfo.status", "Battery is at %d%%", Math.round(status.getCharge() * 100));
    }

    protected Component getTitle() {
        if (!status.isCharging() && status.getCharge() <= lowBatteryThreshold) {
            return Component.translatableWithFallback("toast.batterystatusinfo.lowbattery", "Low Battery").withStyle(ChatFormatting.RED, ChatFormatting.BOLD);
        } else if (status.isCharging()) {
            return Component.translatableWithFallback("toast.batterystatusinfo.charging", "Battery Charging");
        } else {
            return Component.translatableWithFallback("toast.batterystatusinfo.discharging", "Charging Stopped");
        }
    }

    /**
     * Display this toast
     */
    public void show() {
        Minecraft.getInstance().gui.toastManager().addToast(this);
    }

    @Override
    public Object getToken() {
        return Toast.super.getToken();
    }

    @Override
    public int width() {
        // TODO: dynamically calculate width
//        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
//        return Math.max(textRenderer.getWidth(getTitle()), textRenderer.getWidth(getSub()));
        return Toast.super.width();
    }

    @Override
    public int height() {
        return Toast.super.height();
    }

    @Override
    public int occcupiedSlotCount() {
        return Toast.super.occcupiedSlotCount();
    }
}
