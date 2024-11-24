package dev.hbop.balancedtransport.item;

import dev.hbop.balancedtransport.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class ModItems {

    public static final Item SUPERPOWERED_RAIL = Items.register(ModBlocks.SUPERPOWERED_RAIL);
    public static final Item DIRECTIONAL_RAIL = Items.register(ModBlocks.DIRECTIONAL_RAIL);
    
    public static void registerItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register((itemGroup) -> {
            itemGroup.addAfter(Items.POWERED_RAIL, SUPERPOWERED_RAIL);
            itemGroup.addAfter(SUPERPOWERED_RAIL, DIRECTIONAL_RAIL);
        });
    }
}