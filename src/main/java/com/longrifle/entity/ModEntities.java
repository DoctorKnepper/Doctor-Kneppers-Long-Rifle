package com.longrifle.entity;

import com.longrifle.LongRifleMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;

public final class ModEntities {
	private static final ResourceKey<EntityType<?>> IRON_BULLET_KEY = ResourceKey.create(Registries.ENTITY_TYPE, LongRifleMod.id("iron_bullet"));
	public static final EntityType<IronBulletEntity> IRON_BULLET = Registry.register(
		BuiltInRegistries.ENTITY_TYPE,
		IRON_BULLET_KEY,
		Builder.<IronBulletEntity>of(IronBulletEntity::new, MobCategory.MISC)
			.noLootTable()
			.sized(0.15F, 0.15F)
			.clientTrackingRange(8)
			.updateInterval(1)
			.build(IRON_BULLET_KEY)
	);

	private ModEntities() {
	}

	public static void init() {
	}
}
