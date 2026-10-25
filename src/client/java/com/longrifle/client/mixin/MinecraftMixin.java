package com.longrifle.client.mixin;

import com.longrifle.client.LongRifleClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Inject(method = "handleKeybinds", at = @At("HEAD"))
	private void longrifle$pullTrigger(CallbackInfo ci) {
		Minecraft minecraft = (Minecraft)(Object)this;
		if (minecraft.player == null || !LongRifleClient.isHoldingRifle(minecraft.player)) {
			return;
		}

		boolean clicked = false;

		while (minecraft.options.keyAttack.consumeClick()) {
			clicked = true;
		}

		if (clicked) {
			LongRifleClient.fire(minecraft.player);
		}
	}

	@Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
	private void longrifle$noMiningWithRifle(boolean down, CallbackInfo ci) {
		Minecraft minecraft = (Minecraft)(Object)this;
		if (minecraft.player != null && LongRifleClient.isHoldingRifle(minecraft.player)) {
			ci.cancel();
		}
	}
}
