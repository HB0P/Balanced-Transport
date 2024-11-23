package dev.hbop.balancedtransport.mixin.minecart;

import dev.hbop.balancedtransport.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.entity.vehicle.MinecartController;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ExperimentalMinecartController.class)
public abstract class M_ExperimentalMinecartController extends MinecartController {

    protected M_ExperimentalMinecartController(AbstractMinecartEntity minecart) {
        super(minecart);
    }
    
    // change minecart max speed when on superpowered rails
    @Redirect(
            method = "getMaxSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/GameRules;getInt(Lnet/minecraft/world/GameRules$Key;)I"
            )
    )
    private int getMaxSpeed(GameRules instance, GameRules.Key<GameRules.IntRule> rule) {
        BlockState railState = minecart.getWorld().getBlockState(minecart.getRailOrMinecartPos());
        if (railState.isOf(ModBlocks.SUPERPOWERED_RAIL)) {
            return 20;
        }
        return 8;
    }
    
    @Redirect(
            method = "accelerateFromPoweredRail",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean accelerateFromPoweredRail(BlockState instance, Block block) {
        return instance.isOf(Blocks.POWERED_RAIL) || instance.isOf(ModBlocks.SUPERPOWERED_RAIL);
    }

    @Redirect(
            method = "decelerateFromPoweredRail",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean decelerateFromPoweredRail(BlockState instance, Block block) {
        return instance.isOf(Blocks.POWERED_RAIL) || instance.isOf(ModBlocks.SUPERPOWERED_RAIL);
    }
}