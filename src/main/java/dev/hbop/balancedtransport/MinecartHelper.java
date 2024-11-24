package dev.hbop.balancedtransport;

import dev.hbop.balancedtransport.block.DirectionalRailBlock;
import dev.hbop.balancedtransport.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.util.math.Vec3d;

public class MinecartHelper {
    
    public static boolean isTravellingBackwards(Vec3d velocity, BlockState state) {
        if (!state.isOf(ModBlocks.DIRECTIONAL_RAIL)) return false;
        if (velocity.length() < 0.01) return false;
        
        RailShape railShape = state.get(PoweredRailBlock.SHAPE);
        boolean isReversed = state.get(DirectionalRailBlock.REVERSED);
        if (isEastWest(railShape)) {
            return isReversed == velocity.x > 0;
        } else if (isNorthSouth(railShape)) {
            return isReversed == velocity.z > 0;
        }
        throw new RuntimeException("Invalid rail state found");
    }
    
    public static boolean isAcceleratingRail(BlockState state) {
        return state.isOf(Blocks.POWERED_RAIL) || state.isOf(ModBlocks.SUPERPOWERED_RAIL) || state.isOf(ModBlocks.DIRECTIONAL_RAIL);
    }
    
    public static boolean isEastWest(RailShape shape) {
        return shape == RailShape.EAST_WEST || shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST;
    }

    public static boolean isNorthSouth(RailShape shape) {
        return shape == RailShape.NORTH_SOUTH || shape == RailShape.ASCENDING_NORTH || shape == RailShape.ASCENDING_SOUTH;
    }
}