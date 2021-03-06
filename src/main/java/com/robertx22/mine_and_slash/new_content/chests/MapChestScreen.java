package com.robertx22.mine_and_slash.new_content.chests;

import com.mojang.blaze3d.systems.RenderSystem;
import com.robertx22.mine_and_slash.mmorpg.Ref;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MapChestScreen extends ContainerScreen<MapChestContainer> implements IHasContainer<MapChestContainer> {

    private int textureXSize;
    private int textureYSize;

    public MapChestScreen(MapChestContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);

        this.xSize = 176;
        this.ySize = 166;

        this.textureXSize = 256;
        this.textureYSize = 256;

        this.passEvents = false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName()
            .getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
    }

    private static final ResourceLocation texture = new ResourceLocation(Ref.MODID, "textures/gui/chest.png");

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.minecraft.getTextureManager()
            .bindTexture(texture);

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        blit(x, y, 0, 0, this.xSize, this.ySize, this.textureXSize, this.textureYSize);
    }
}