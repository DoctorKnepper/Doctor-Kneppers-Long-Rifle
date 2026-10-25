package com.longrifle.client;

import com.longrifle.entity.ModEntities;
import com.longrifle.item.LongRifleItem;
import com.longrifle.item.ModItems;
import com.longrifle.network.FireRiflePayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.ItemStack;

public class LongRifleClient implements ClientModInitializer {
	public static final float SCOPE_FOV_MODIFIER = 0.25F;

	@Override
	public void onInitializeClient() {
		EntityRenderers.register(ModEntities.IRON_BULLET, context -> new ThrownItemRenderer<>(context, 0.5F, true));
	}

	public static boolean isHoldingRifle(LocalPlayer player) {
		return player.getMainHandItem().is(ModItems.LONG_RIFLE);
	}

	public static void fire(LocalPlayer player) {
		ItemStack rifle = player.getMainHandItem();
		if (player.getCooldowns().isOnCooldown(rifle)) {
			return;
		}

		ClientPlayNetworking.send(FireRiflePayload.INSTANCE);
		if (LongRifleItem.hasAmmo(player)) {
			boolean scoped = LongRifleItem.isScoped(player);
			float kick = scoped ? 2.0F : 4.0F;
			player.setXRot(player.getXRot() - kick);
			player.setYRot(player.getYRot() + (player.getRandom().nextFloat() - 0.5F) * kick * 0.5F);
		}
	}
}
