package com.github.notroy.astralconsumables.mixin;

import com.github.notroy.astralconsumables.effect.ModEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityMixin {
	// Sticky hands
	@Redirect(
		method = "move",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/TagKey;)Z"
		)
	)
	private boolean isIn(BlockState instance, TagKey<Block> tagKey) {
		Entity self = (Entity) (Object) this;
		if (tagKey == BlockTags.CLIMBABLE && self.isLiving()) {
			LivingEntity livingSelf = (LivingEntity) self;
			StatusEffectInstance stickyHands = livingSelf.getStatusEffect(ModEffects.STICKY_HANDS);
			if (stickyHands != null && stickyHands.getAmplifier() > 0 && stickyHands.getDuration() > 0) {
				return true;
			}
		}
		return instance.isIn(tagKey);
	}
	
	// Bouncy
	private void bounce(Entity entity) {
		Vec3d vec3d = entity.getVelocity();
		if (vec3d.y < 0.0) {
			double d = entity instanceof LivingEntity ? 1.0 : 0.8;
			entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);
		}
	}
	
	// Bouncy
	@Redirect(
		method = "move",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/Block;onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V"
		)
	)
	private void onEntityLand(Block instance, BlockView world, Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			StatusEffectInstance bouncy = livingEntity.getStatusEffect(ModEffects.BOUNCY);
			if (bouncy != null && bouncy.getAmplifier() >= 0 && bouncy.getDuration() > 0) {
				if (livingEntity instanceof PlayerEntity player) {
					if (!player.isSneaking()) {
						bounce(entity);
						return;
					}
				} else {
					bounce(entity);
					return;
				}
			}
		}
		
		instance.onEntityLand(world, entity);
	}
	
	// Bouncy
	@Redirect(
		method = "fall",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"
		)
	)
	protected void onLandedUpon(Block instance, World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (entity instanceof LivingEntity livingEntity) {
			StatusEffectInstance bouncy = livingEntity.getStatusEffect(ModEffects.BOUNCY);
			if (bouncy != null && bouncy.getAmplifier() >= 0 && bouncy.getDuration() > 0) {
				if (livingEntity instanceof PlayerEntity player) {
					if (!player.isSneaking()) {
						return;
					}
				} else {
					return;
				}
			}
		}
		
		entity.handleFallDamage(fallDistance, 1.0f, DamageSource.FALL);
	}
}
