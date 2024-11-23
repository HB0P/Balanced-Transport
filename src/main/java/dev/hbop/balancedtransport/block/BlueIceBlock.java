package dev.hbop.balancedtransport.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.TranslucentBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class BlueIceBlock extends TranslucentBlock {
    
    public BlueIceBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getDimension().ultrawarm()) {
            world.removeBlock(pos, false);
        }
        super.randomTick(state, world, pos, random);
    }
}