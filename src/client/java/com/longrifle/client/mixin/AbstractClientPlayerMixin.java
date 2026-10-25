package com.longrifle.client.mixin;

import com.longrifle.client.LongRifleClient;
import com.longrifle.item.LongRifleItem;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {
	@Inject(method = "getFieldOfViewModifier", at = @At("RETURN"), cancellable = true)
	private void longrifle$scopeZoom(boolean firstPerson, float effectScale, CallbackInfoReturnable<Float> cir) {
		if (firstPerson && LongRifleItem.isScoped((AbstractClientPlayer)(Object)this)) {
			cir.setReturnValue(LongRifleClient.SCOPE_FOV_MODIFIER);
		}
	}
}
