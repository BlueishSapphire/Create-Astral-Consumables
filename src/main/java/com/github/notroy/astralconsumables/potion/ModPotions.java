package com.github.notroy.astralconsumables.potion;

import com.github.notroy.astralconsumables.AstralConsumables;
import com.github.notroy.astralconsumables.effect.ModEffects;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModPotions {
	public static Potion BOUNCY_POTION;
	public static Potion LOW_G_POTION;
	public static Potion STICKY_HANDS_POTION;
	
	public static Potion registerPotion(String name, StatusEffect effect, int duration, int amplifier) {
		Potion potion = new Potion(new StatusEffectInstance(effect, duration, amplifier));
		Registry.register(Registry.POTION, new Identifier(AstralConsumables.ID, name), potion);
		return potion;
	}
	
	public static void registerAll() {
		BOUNCY_POTION = registerPotion("bouncy", ModEffects.BOUNCY, 1200, 0);
		LOW_G_POTION = registerPotion("low_g", ModEffects.LOW_G, 1200, 0);
		STICKY_HANDS_POTION = registerPotion("sticky_hands", ModEffects.STICKY_HANDS, 1200, 0);
	}
}
