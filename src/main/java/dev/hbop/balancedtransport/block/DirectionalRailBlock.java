package dev.hbop.balancedtransport.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.Direction;

public class DirectionalRailBlock extends PoweredRailBlock {
    
    public static final BooleanProperty REVERSED = BooleanProperty.of("reversed");

    public DirectionalRailBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(REVERSED, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        assert state != null;
        boolean isReversed = switch (state.get(SHAPE)) {
            case EAST_WEST -> ctx.getHorizontalPlayerFacing() == Direction.WEST;
            case NORTH_SOUTH -> ctx.getHorizontalPlayerFacing() == Direction.NORTH;
            default -> throw new UnsupportedOperationException();
        };
        return state.with(REVERSED, isReversed);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED, REVERSED);
    }
}