package com.robertx22.mine_and_slash.mmorpg.registers.client;

import com.robertx22.mine_and_slash.dimensions.blocks.MapPortalRenderer;
import com.robertx22.mine_and_slash.mmorpg.registers.common.ModTileEntities;
import com.robertx22.mine_and_slash.new_content.chests.MapChestRenderer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class SpecialRenderRegister {

    public static void register(FMLClientSetupEvent event) {

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.MAP_PORTAL.get(), MapPortalRenderer::new);

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.MAP_CHEST.get(), MapChestRenderer::new);

    }

}
