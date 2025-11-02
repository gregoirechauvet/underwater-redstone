package samy.underwaterredstone.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import samy.underwaterredstone.block.RedstoneCableBlock;

@Mixin(RedStoneWireBlock.class)
public class RedstoneWireBlockMixin {
    @Inject(method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private static void allowRedstoneCableConnection(BlockState state, @Nullable Direction direction, CallbackInfoReturnable<Boolean> cir) {
        // Allow connecting to RedstoneCableBlock
        if (state.getBlock() instanceof RedstoneCableBlock) {
            cir.setReturnValue(true);
        }
    }
}