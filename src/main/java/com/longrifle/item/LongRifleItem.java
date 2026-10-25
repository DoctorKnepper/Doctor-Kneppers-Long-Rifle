package com.longrifle.item;

import com.longrifle.entity.IronBulletEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class LongRifleItem extends Item {
	public static final int COOLDOWN_TICKS = 25;
	public static final int DRY_FIRE_COOLDOWN_TICKS = 10;
	public static final float SCOPED_DAMAGE = 18.0F;
	public static final float SCOPED_VELOCITY = 7.0F;
	public static final float SCOPED_INACCURACY = 0.0F;
	public static final float HIP_DAMAGE = 12.0F;
	public static final float HIP_VELOCITY = 5.0F;
	public static final float HIP_INACCURACY = 5.0F;

	public LongRifleItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public int getUseDuration(ItemStack itemStack, LivingEntity user) {
		return 72000;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
		return ItemUseAnimation.NONE;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.PASS;
		}

		player.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 0.8F);
		return ItemUtils.startUsingInstantly(level, player, hand);
	}

	@Override
	public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
		entity.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 0.8F);
		return true;
	}

	@Override
	public boolean canDestroyBlock(ItemStack itemStack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
		return false;
	}

	public static boolean isScoped(Player player) {
		return player.isUsingItem() && player.getUseItem().is(ModItems.LONG_RIFLE);
	}

	public static boolean hasAmmo(Player player) {
		return player.hasInfiniteMaterials() || !findAmmo(player).isEmpty();
	}

	public static void tryFire(ServerPlayer player) {
		ItemStack rifle = player.getMainHandItem();
		if (!rifle.is(ModItems.LONG_RIFLE) || player.isSpectator() || player.getCooldowns().isOnCooldown(rifle)) {
			return;
		}

		ServerLevel level = player.level();
		ItemStack ammo = findAmmo(player);
		if (ammo.isEmpty() && !player.hasInfiniteMaterials()) {
			level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 0.6F, 1.6F);
			player.getCooldowns().addCooldown(rifle, DRY_FIRE_COOLDOWN_TICKS);
			return;
		}

		boolean scoped = isScoped(player);
		IronBulletEntity bullet = new IronBulletEntity(level, player, scoped ? SCOPED_DAMAGE : HIP_DAMAGE);
		bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, scoped ? SCOPED_VELOCITY : HIP_VELOCITY, scoped ? SCOPED_INACCURACY : HIP_INACCURACY);
		level.addFreshEntity(bullet);
		Vec3 look = player.getLookAngle();
		Vec3 right = look.cross(new Vec3(0.0, 1.0, 0.0)).normalize();
		Vec3 muzzle = player.getEyePosition().add(look.scale(1.6)).add(right.scale(scoped ? 0.0 : 0.4)).add(0.0, scoped ? -0.25 : -0.35, 0.0);
		level.sendParticles(ParticleTypes.SMOKE, muzzle.x, muzzle.y, muzzle.z, 3, 0.02, 0.02, 0.02, 0.01);
		level.sendParticles(ParticleTypes.SMALL_FLAME, muzzle.x, muzzle.y, muzzle.z, 1, 0.0, 0.0, 0.0, 0.0);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, SoundSource.PLAYERS, 2.0F, 0.6F);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.5F, 1.9F);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 0.7F, 0.7F);
		if (!player.hasInfiniteMaterials()) {
			ammo.shrink(1);
		}

		rifle.hurtAndBreak(1, player, InteractionHand.MAIN_HAND);
		player.getCooldowns().addCooldown(rifle, COOLDOWN_TICKS);
		player.awardStat(Stats.ITEM_USED.get(ModItems.LONG_RIFLE));
	}

	private static ItemStack findAmmo(Player player) {
		ItemStack offhand = player.getOffhandItem();
		if (offhand.is(ModItems.IRON_BULLET)) {
			return offhand;
		}

		for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
			ItemStack stack = player.getInventory().getItem(slot);
			if (stack.is(ModItems.IRON_BULLET)) {
				return stack;
			}
		}

		return ItemStack.EMPTY;
	}
}
