package dev.hbop.balancedtransport.mixin.minecart;

import dev.hbop.balancedtransport.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public abstract class M_AbstractMinecartEntity {
    
    // force minecart improvements on
    @Inject(
            method = "areMinecartImprovementsEnabled",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void areMinecartImprovementsEnabled(World world, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
    
    @Redirect(
            method = "getLaunchDirection",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean getLaunchDirection(BlockState instance, Block block) {
        return instance.isOf(Blocks.POWERED_RAIL) || instance.isOf(ModBlocks.SUPERPOWERED_RAIL);
    }
}