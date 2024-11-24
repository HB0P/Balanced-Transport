package dev.hbop.balancedtransport.mixin.minecart;

import dev.hbop.balancedtransport.MinecartHelper;
import dev.hbop.balancedtransport.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.entity.vehicle.MinecartController;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    // allow acceleration from superpowered and directional rails
    @Redirect(
            method = "accelerateFromPoweredRail",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean accelerateFromPoweredRail(BlockState instance, Block block) {
        return MinecartHelper.isAcceleratingRail(instance);
    }
    
    // do not accelerate from directional rails in the wrong direction
    @Inject(
            method = "accelerateFromPoweredRail",
            at = @At("HEAD"),
            cancellable = true
    )
    private void accelerateFromPoweredRail(Vec3d velocity, BlockPos railPos, BlockState railState, CallbackInfoReturnable<Vec3d> cir) {
        if (MinecartHelper.isTravellingBackwards(velocity, railState)) {
            cir.setReturnValue(velocity);
        }
    }
    
    // allow deceleration from superpowered and directional rails
    @Redirect(
            method = "decelerateFromPoweredRail",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean decelerateFromPoweredRail(BlockState instance, Block block) {
        return MinecartHelper.isAcceleratingRail(instance);
    }
    
    // decelerate from directional rails in the wrong direction
    @Inject(
            method = "decelerateFromPoweredRail",
            at = @At("HEAD"),
            cancellable = true
    )
    private void decelerateFromPoweredRail(Vec3d velocity, BlockState railState, CallbackInfoReturnable<Vec3d> cir) {
        if (MinecartHelper.isTravellingBackwards(velocity, railState)) {
            cir.setReturnValue(velocity.length() < 0.03 ? Vec3d.ZERO : velocity.multiply(0.5));
        }
    }
}