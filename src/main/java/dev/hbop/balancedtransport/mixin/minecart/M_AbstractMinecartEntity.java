package dev.hbop.balancedtransport.mixin.minecart;

import dev.hbop.balancedtransport.MinecartHelper;
import dev.hbop.balancedtransport.block.DirectionalRailBlock;
import dev.hbop.balancedtransport.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public abstract class M_AbstractMinecartEntity extends VehicleEntity {

    public M_AbstractMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    // force minecart improvements on
    @Inject(
            method = "areMinecartImprovementsEnabled",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void areMinecartImprovementsEnabled(World world, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
    
    // set directional rail launching behaviour
    @Inject(
            method = "getLaunchDirection",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getLaunchDirection(BlockPos railPos, CallbackInfoReturnable<Vec3d> cir) {
        BlockState blockState = this.getWorld().getBlockState(railPos);
        if (blockState.isOf(ModBlocks.DIRECTIONAL_RAIL) && blockState.get(PoweredRailBlock.POWERED)) {
            RailShape railShape = blockState.get(PoweredRailBlock.SHAPE);
            boolean isReversed = blockState.get(DirectionalRailBlock.REVERSED);
            if (MinecartHelper.isEastWest(railShape)) {
                cir.setReturnValue(new Vec3d(isReversed ? -1 : 1, 0, 0));
            } else if (MinecartHelper.isNorthSouth(railShape)) {
                cir.setReturnValue(new Vec3d(0, 0, isReversed ? -1 : 1));
            }
        }
    }
    
    // allow superpowered rails to launch minecarts
    @Redirect(
            method = "getLaunchDirection",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean getLaunchDirection(BlockState instance, Block block) {
        return MinecartHelper.isAcceleratingRail(instance);
    }
}