package com.longrifle.item;

import com.longrifle.LongRifleMod;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item.Properties;

public final class ModItems {
	public static final Item LONG_RIFLE = register("long_rifle", LongRifleItem::new, new Properties().durability(465).repairable(Items.IRON_INGOT));
	public static final Item IRON_BULLET = register("iron_bullet", Item::new, new Properties().stacksTo(64));

	private ModItems() {
	}

	private static Item register(String name, Function<Properties, Item> factory, Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, LongRifleMod.id(name));
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
	}

	public static void init() {
	}
}
