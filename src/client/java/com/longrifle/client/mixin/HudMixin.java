package com.longrifle.client.mixin;

import com.longrifle.LongRifleMod;
import com.longrifle.item.LongRifleItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class HudMixin {
	@Unique
	private static final Identifier RIFLE_SCOPE_LOCATION = LongRifleMod.id("textures/misc/rifle_scope.png");

	@Inject(method = "extractSpyglassOverlay", at = @At("HEAD"), cancellable = true)
	private void longrifle$rifleScopeOverlay(GuiGraphicsExtractor graphics, float scale, CallbackInfo ci) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null || !LongRifleItem.isScoped(minecraft.player)) {
			return;
		}

		int size = Mth.floor(Math.min(graphics.guiWidth(), graphics.guiHeight()) * scale);
		int left = (graphics.guiWidth() - size) / 2;
		int top = (graphics.guiHeight() - size) / 2;
		int right = left + size;
		int bottom = top + size;
		graphics.blit(RenderPipelines.GUI_TEXTURED, RIFLE_SCOPE_LOCATION, left, top, 0.0F, 0.0F, size, size, size, size);
		graphics.fill(RenderPipelines.GUI, 0, bottom, graphics.guiWidth(), graphics.guiHeight(), 0xFF000000);
		graphics.fill(RenderPipelines.GUI, 0, 0, graphics.guiWidth(), top, 0xFF000000);
		graphics.fill(RenderPipelines.GUI, 0, top, left, bottom, 0xFF000000);
		graphics.fill(RenderPipelines.GUI, right, top, graphics.guiWidth(), bottom, 0xFF000000);
		ci.cancel();
	}
}
