/*
 * Copyright (c) 2023 Edgeburn Media. All rights reserved.
 */

package com.edgeburnmedia.batterystatusinfo.toast;

import com.edgeburnmedia.batterystatusinfo.BatteryStatus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class BatteryAlertToast implements Toast {
    private static final Identifier BACKGROUND_TEXTURE = new Identifier("minecraft", "textures/gui/toasts.png");
    private static final double DISPLAY_TIME = 3000;
    private static final int WHITE_COLOUR = 0xFFFFFF;
    private static final int GRAY_COLOUR = 0xAAAAAA;
    private final Identifier iconTexture;
    private final BatteryStatus status;
    private double lowBatteryThreshold;
    private long startTime;

    public BatteryAlertToast(BatteryStatus status, double lowBatteryThreshold) {
        this.iconTexture = status.getBatteryIcon();
        this.status = status;
        this.lowBatteryThreshold = lowBatteryThreshold;
    }
    @Override
    public Visibility draw(DrawContext context, ToastManager manager, long startTime) {
        context.drawTexture(BACKGROUND_TEXTURE, 0,0,0,0,160, 32);

        context.drawTexture(iconTexture, 4,5,0,0, 21,21, 21, 21);
        // 4 + 21 + 2 = x of icon texture + width of icon texture + buffer space
        context.drawText(MinecraftClient.getInstance().textRenderer, getTitle(), 4+21+2,7, WHITE_COLOUR, false);

        context.drawText(MinecraftClient.getInstance().textRenderer, getSub(), 4+21+2, 7+MinecraftClient.getInstance().textRenderer.fontHeight, GRAY_COLOUR, false);

        return startTime > DISPLAY_TIME ? Visibility.HIDE : Visibility.SHOW;
    }

    protected Text getSub() {
        return Text.translatableWithFallback("toast.batterystatusinfo.status", "Battery is at %d%%", Math.round(status.getCharge() * 100));
    }

    protected Text getTitle() {
        if (!status.isCharging() && status.getCharge() <= lowBatteryThreshold) {
            return Text.translatableWithFallback("toast.lowbatterystatus.lowbattery", "Low Battery").formatted(Formatting.RED, Formatting.BOLD);
        } else if (status.isCharging()) {
            return Text.translatableWithFallback("toast.batterystatusinfo.charging", "Battery Charging");
        } else if (!status.isCharging()) {
            return Text.translatableWithFallback("toast.batterystatusinfo.discharging", "Charging Stopped");
        } else {
            return Text.translatableWithFallback("toast.batterystatusinfo.generic", "Battery Alert");
        }
    }

    /**
     * Display this toast
     */
    public void show() {
        MinecraftClient.getInstance().getToastManager().add(this);
    }

    @Override
    public Object getType() {
        return Toast.super.getType();
    }

    @Override
    public int getWidth() {
        // TODO: dynamically calculate width
//        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
//        return Math.max(textRenderer.getWidth(getTitle()), textRenderer.getWidth(getSub()));
        return Toast.super.getWidth();
    }

    @Override
    public int getHeight() {
        return Toast.super.getHeight();
    }

    @Override
    public int getRequiredSpaceCount() {
        return Toast.super.getRequiredSpaceCount();
    }
}
