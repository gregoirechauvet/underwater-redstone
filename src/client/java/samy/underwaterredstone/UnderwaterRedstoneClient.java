package samy.underwaterredstone;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.RedStoneWireBlock;


public class UnderwaterRedstoneClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT_MIPPED, UnderwaterRedstone.REDSTONE_CABLE_BLOCK);

        ColorProviderRegistry.BLOCK.register(
                (blockState, blockAndTintGetter, blockPos, i) -> RedStoneWireBlock.getColorForPower(blockState.getValue(RedStoneWireBlock.POWER)),
                UnderwaterRedstone.REDSTONE_CABLE_BLOCK
        );
    }
}