package com.github.notroy.astralconsumables.items;

import com.github.notroy.astralconsumables.AstralConsumables;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;

public class ModItems {
	public static Item RECALL;
//	public static Item WORMHOLE;
	
	public static Item registerItem(String name, Function<Item.Settings, Item> creator) {
		Item item = creator.apply(new FabricItemSettings());
		Registry.register(Registry.ITEM, new Identifier(AstralConsumables.ID, name), item);
		return item;
	}
	
	public static void registerAll() {
		RECALL = registerItem("recall", RecallItem::new);
//		WORMHOLE = registerItem("wormhole", WormholeItem::new);
	}
}
