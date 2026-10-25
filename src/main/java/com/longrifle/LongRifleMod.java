package com.longrifle;

import com.longrifle.entity.ModEntities;
import com.longrifle.item.LongRifleItem;
import com.longrifle.item.ModItems;
import com.longrifle.network.FireRiflePayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LongRifleMod implements ModInitializer {
	public static final String MOD_ID = "longrifle";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.init();
		ModEntities.init();
		PayloadTypeRegistry.serverboundPlay().register(FireRiflePayload.TYPE, FireRiflePayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(FireRiflePayload.TYPE, (payload, context) -> LongRifleItem.tryFire(context.player()));
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
			.register(output -> output.insertAfter(Items.CROSSBOW, ModItems.LONG_RIFLE, ModItems.IRON_BULLET));
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
