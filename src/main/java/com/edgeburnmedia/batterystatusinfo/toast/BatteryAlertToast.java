/*
 * Copyright (c) 2023-2024 Edgeburn Media. All rights reserved.
 */

package com.edgeburnmedia.batterystatusinfo.toast;

import com.edgeburnmedia.batterystatusinfo.BatteryStatus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class BatteryAlertToast implements Toast {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.of("minecraft", "toast/advancement");
    private static final double DISPLAY_TIME = 3000;
    private static final int WHITE_COLOUR = Colors.WHITE;
    private static final int GRAY_COLOUR = Colors.LIGHT_GRAY;
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
    public void draw(DrawContext context, TextRenderer textRenderer, long startTime) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, 0, 1, 160, 32);

        context.drawTexture(RenderPipelines.GUI_TEXTURED, iconTexture, 4,5,0,0, 21,21, 21, 21);
        // 4 + 21 + 2 = x of icon texture + width of icon texture + buffer space
        context.drawText(MinecraftClient.getInstance().textRenderer, getTitle(), 4+21+2,7, WHITE_COLOUR, false);

        context.drawText(textRenderer, getSub(), 4+21+2, 7+textRenderer.fontHeight, GRAY_COLOUR, false);
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    protected Text getSub() {
        return Text.translatableWithFallback("toast.batterystatusinfo.status", "Battery is at %d%%", Math.round(status.getCharge() * 100));
    }

    protected Text getTitle() {
        if (!status.isCharging() && status.getCharge() <= lowBatteryThreshold) {
            return Text.translatableWithFallback("toast.batterystatusinfo.lowbattery", "Low Battery").formatted(Formatting.RED, Formatting.BOLD);
        } else if (status.isCharging()) {
            return Text.translatableWithFallback("toast.batterystatusinfo.charging", "Battery Charging");
        } else {
            return Text.translatableWithFallback("toast.batterystatusinfo.discharging", "Charging Stopped");
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
