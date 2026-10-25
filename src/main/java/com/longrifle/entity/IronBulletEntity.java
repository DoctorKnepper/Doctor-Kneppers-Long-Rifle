package com.longrifle.entity;

import com.longrifle.LongRifleMod;
import com.longrifle.item.ModItems;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class IronBulletEntity extends ThrowableItemProjectile {
	public static final ResourceKey<DamageType> BULLET_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, LongRifleMod.id("bullet"));
	private static final int MAX_LIFETIME_TICKS = 80;
	private float damage = 12.0F;

	public IronBulletEntity(EntityType<? extends IronBulletEntity> type, Level level) {
		super(type, level);
	}

	public IronBulletEntity(Level level, LivingEntity owner, float damage) {
		super(ModEntities.IRON_BULLET, owner, level, new ItemStack(ModItems.IRON_BULLET));
		this.damage = damage;
	}

	@Override
	protected Item getDefaultItem() {
		return ModItems.IRON_BULLET;
	}

	@Override
	protected double getDefaultGravity() {
		return 0.006;
	}

	@Override
	protected float getAirDrag() {
		return 0.995F;
	}

	@Override
	public void tick() {
		Vec3 start = this.position();
		super.tick();
		if (this.level().isClientSide()) {
			if (this.tickCount < 2) {
				return;
			}

			Vec3 end = this.position();

			for (int i = 0; i < 4; i++) {
				Vec3 point = start.lerp(end, i / 4.0);
				this.level().addParticle(ParticleTypes.WHITE_SMOKE, point.x, point.y, point.z, 0.0, 0.0, 0.0);
			}
		} else if (this.tickCount > MAX_LIFETIME_TICKS) {
			this.discard();
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		super.onHitEntity(hitResult);
		if (this.level() instanceof ServerLevel serverLevel) {
			DamageSource source = new DamageSource(serverLevel.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(BULLET_DAMAGE), this, this.getOwner());
			hitResult.getEntity().hurtServer(serverLevel, source, this.damage);
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult hitResult) {
		super.onHitBlock(hitResult);
		if (this.level() instanceof ServerLevel serverLevel) {
			BlockState state = serverLevel.getBlockState(hitResult.getBlockPos());
			Vec3 at = hitResult.getLocation();
			serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), at.x, at.y, at.z, 8, 0.05, 0.05, 0.05, 0.1);
			serverLevel.playSound(null, at.x, at.y, at.z, state.getSoundType().getHitSound(), SoundSource.BLOCKS, 1.0F, 1.2F);
		}
	}

	@Override
	protected void onHit(HitResult hitResult) {
		super.onHit(hitResult);
		if (!this.level().isClientSide()) {
			this.discard();
		}
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putFloat("damage", this.damage);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.damage = input.getFloatOr("damage", 12.0F);
	}
}
