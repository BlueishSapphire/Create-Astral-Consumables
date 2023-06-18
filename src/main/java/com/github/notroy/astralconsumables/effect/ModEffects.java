package com.github.notroy.astralconsumables.effect;

import com.github.notroy.astralconsumables.AstralConsumables;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.BiFunction;

public class ModEffects {
	public static StatusEffect BOUNCY;
	public static StatusEffect LOW_G;
	public static StatusEffect STICKY_HANDS;
	
	public static StatusEffect registerEffect(String name, BiFunction<StatusEffectCategory, Integer, StatusEffect> creator, StatusEffectCategory category, int color) {
		StatusEffect effect = creator.apply(category, color);
		Registry.register(Registry.STATUS_EFFECT, new Identifier(AstralConsumables.ID, name), effect);
		return effect;
	}
	
	public static void registerAll() {
		BOUNCY = registerEffect("bouncy", Bouncy::new, StatusEffectCategory.NEUTRAL, 0);
		LOW_G = registerEffect("low_g", LowG::new, StatusEffectCategory.NEUTRAL, 0);
		STICKY_HANDS = registerEffect("sticky_hands", StickyHands::new, StatusEffectCategory.NEUTRAL, 0);
	}
}
