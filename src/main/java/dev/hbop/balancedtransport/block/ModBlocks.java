package dev.hbop.balancedtransport.block;

import dev.hbop.balancedtransport.BalancedTransport;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {

    public static final Block SUPERPOWERED_RAIL = Blocks.register(
            RegistryKey.of(RegistryKeys.BLOCK, BalancedTransport.identifier("superpowered_rail")), 
            PoweredRailBlock::new, 
            AbstractBlock.Settings.create().noCollision().strength(0.7F).sounds(BlockSoundGroup.METAL)
    );
    
    public static void registerBlocks() {
        
    }
}