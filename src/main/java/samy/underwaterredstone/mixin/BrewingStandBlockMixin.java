package samy.underwaterredstone.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingStandBlock.class)
@Implements(@Interface(iface = SimpleWaterloggedBlock.class, prefix = "waterlogged$"))
public class BrewingStandBlockMixin extends Block {
    @Unique
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    // Constructor required for extending Block (never actually called)
    private BrewingStandBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * Inject into createBlockStateDefinition to add the WATERLOGGED property
     */
    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    protected void addWaterloggedProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(WATERLOGGED);
    }

    /**
     * Inject into the constructor to set the default WATERLOGGED state to false
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    private void setDefaultWaterloggedState(BlockBehaviour.Properties properties, CallbackInfo ci) {
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
    }

    /**
     * Override getStateForPlacement to check for water and set WATERLOGGED accordingly
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockState state = this.defaultBlockState();
        return state.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    /**
     * Override getFluidState to return water when waterlogged
     */
    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(state);
    }

    /**
     * Override updateShape to schedule water ticks when waterlogged
     */
    @Override
    public @NotNull BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess,
                                           BlockPos pos, Direction direction, BlockPos neighborPos,
                                           BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
    }
}
