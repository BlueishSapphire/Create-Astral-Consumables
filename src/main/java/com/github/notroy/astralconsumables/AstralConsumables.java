package com.github.notroy.astralconsumables;

import com.github.notroy.astralconsumables.effect.ModEffects;
import com.github.notroy.astralconsumables.items.ModItems;
import com.github.notroy.astralconsumables.potion.ModPotions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Identifier;

import java.util.List;

public class AstralConsumables implements ModInitializer {
	public static final String ID = "astral_consumables";
	
	public static ItemGroup mainItemGroup;
	
	@Override
	public void onInitialize() {
		ModEffects.registerAll();
		ModPotions.registerAll();
		ModItems.registerAll();
		
		registerItemGroups();
	}
	
	public static void registerItemGroups() {
		mainItemGroup = FabricItemGroupBuilder
			.create(new Identifier(ID, "main_group"))
			.appendItems((List<ItemStack> stacksForDisplay) -> {
				stacksForDisplay.add(ModItems.RECALL.getDefaultStack());
				stacksForDisplay.add(PotionUtil.setPotion(Items.POTION.getDefaultStack(), ModPotions.BOUNCY_POTION));
				stacksForDisplay.add(PotionUtil.setPotion(Items.POTION.getDefaultStack(), ModPotions.LOW_G_POTION));
				stacksForDisplay.add(PotionUtil.setPotion(Items.POTION.getDefaultStack(), ModPotions.STICKY_HANDS_POTION));
			})
			.icon(ModItems.RECALL::getDefaultStack)
			.build();
	}
}
