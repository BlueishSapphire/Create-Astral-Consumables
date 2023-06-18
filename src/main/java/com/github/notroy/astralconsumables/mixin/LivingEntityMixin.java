package com.github.notroy.astralconsumables.mixin;

import com.github.notroy.astralconsumables.effect.ModEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	// Low G
	@ModifyVariable(method = "travel", at = @At("STORE"), ordinal = 0)
	private double injected(double d) {
		LivingEntity self = (LivingEntity) (Object) this;
		StatusEffectInstance lowG = self.getStatusEffect(ModEffects.LOW_G);
		if (lowG != null && lowG.getDuration() > 0) {
			return d * Math.pow(0.5, lowG.getAmplifier() + 1);
		} else {
			return d;
		}
	}
	
	// Sticky hands
	@Redirect(
		method = "isClimbing",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/TagKey;)Z"
		)
	)
	private boolean isIn(BlockState instance, TagKey<Block> tagKey) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (tagKey == BlockTags.CLIMBABLE) {
			StatusEffectInstance stickyHands = self.getStatusEffect(ModEffects.STICKY_HANDS);
			if (stickyHands != null && stickyHands.getAmplifier() > 0 && stickyHands.getDuration() > 0) {
				return true;
			}
		}
		return instance.isIn(tagKey);
	}
}
