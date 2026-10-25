package com.longrifle.mixin;

import com.longrifle.item.LongRifleItem;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
	@Inject(method = "isScoping", at = @At("RETURN"), cancellable = true)
	private void longrifle$scopeWithRifle(CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && LongRifleItem.isScoped((Player)(Object)this)) {
			cir.setReturnValue(true);
		}
	}
}
