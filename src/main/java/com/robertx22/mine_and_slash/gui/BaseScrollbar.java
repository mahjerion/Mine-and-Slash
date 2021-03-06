package com.robertx22.mine_and_slash.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.robertx22.mine_and_slash.mmorpg.Ref;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public abstract class BaseScrollbar extends Widget {
    static ResourceLocation SCROLLBAR_TEXTURE = new ResourceLocation(Ref.MODID, "textures/gui/scrollbar.png");

    static int sizeY = 27;
    static int sizeX = 6;
    protected double value = 0; // 0-1

    int totalHeight;

    protected BaseScrollbar(int xpos, int ypos, int scrollbarTotalHeight) {
        super(xpos, ypos, sizeX, scrollbarTotalHeight, "");

        this.totalHeight = scrollbarTotalHeight;
    }

    @Override
    protected void renderBg(Minecraft mc, int x, int y) {
        mc.getTextureManager()
            .bindTexture(SCROLLBAR_TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int xadd = (this.isHovered() ? 1 : 0) * sizeX;

        int yPos = getScrollBarY();

        this.blit(this.x, this.y, 50, 0, sizeX, totalHeight); // draw grey background

        this.blit(this.x, yPos, xadd, 0, sizeX, sizeY); // draw the scrollbar

    }

    public int getScrollBarY() {

        int val = (int) ((float) this.y + (value * (float) totalHeight));

        return MathHelper.clamp(val - sizeY / 2, this.y, this.y + totalHeight - sizeY);
    }

    @Override
    public void renderButton(int x, int y, float f) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fontrenderer = mc.fontRenderer;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        this.renderBg(mc, x, y);
    }

    @Override
    public void onClick(double x, double y) {
        this.setValueFromMouse(y);
    }

    private void setValueFromMouse(double y) {
        this.setValue(((y - (double) (this.y)) / (double) (this.totalHeight)));
    }

    private void setValue(double y) {
        double val = this.value;
        this.value = MathHelper.clamp(y, 0.0D, 1.0D);
        if (val != this.value) {
            this.applyValue();
        }
    }

    public void setValueFromElement(int element, int max) {
        this.value = (float) element / (float) max;
    }

    @Override
    protected void onDrag(double x, double y, double f3, double f4) {
        this.setValueFromMouse(y);
        super.onDrag(x, y, f3, f4);
    }

    @Override
    public void onRelease(double x, double y) {
        super.playDownSound(Minecraft.getInstance()
            .getSoundHandler());
    }

    protected abstract void applyValue();
}

