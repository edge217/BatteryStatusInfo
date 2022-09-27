package com.edgeburnmedia.batterystatusinfo.mixin;

import com.edgeburnmedia.batterystatusinfo.client.BatteryStatusInfoModClient;
import net.minecraft.resource.SimpleResourceReload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(SimpleResourceReload.class)
public class SimpleResourceReloadMixin {

	@Inject(at = @At("RETURN"), method = "whenComplete")
	public void whenComplete(CallbackInfoReturnable<CompletableFuture<?>> cir) {
		if (cir.getReturnValue().isDone()) {
			BatteryStatusInfoModClient.gameResourcesReady();
		}
	}
}
