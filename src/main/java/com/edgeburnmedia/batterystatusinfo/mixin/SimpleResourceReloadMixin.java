package com.edgeburnmedia.batterystatusinfo.mixin;

import com.edgeburnmedia.batterystatusinfo.client.BatteryStatusInfoModClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import net.minecraft.server.packs.resources.SimpleReloadInstance;

@Mixin(SimpleReloadInstance.class)
public class SimpleResourceReloadMixin {

	@Inject(at = @At("RETURN"), method = "done")
	public void done(CallbackInfoReturnable<CompletableFuture<?>> cir) {
		if (cir.getReturnValue().isDone()) {
			BatteryStatusInfoModClient.gameResourcesReady();
		}
	}
}
