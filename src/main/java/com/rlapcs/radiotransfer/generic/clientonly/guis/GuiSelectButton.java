package com.rlapcs.radiotransfer.generic.clientonly.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


//this should eventually be an inner class of the superGui we make for the transmitter / receiver
@SideOnly(Side.CLIENT)
class GuiSelectButton extends GuiButton {
    private final ResourceLocation iconTexture;
    private final int iconX;
    private final int iconY;
    private boolean selected;

    protected GuiSelectButton(int buttonId, int x, int y, ResourceLocation iconTextureIn, int iconXIn, int iconYIn) {
        super(buttonId, x, y, 22, 22, "");
        this.iconTexture = iconTextureIn;
        this.iconX = iconXIn;
        this.iconY = iconYIn;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            //mc.getTextureManager().bindTexture(GuiBeacon.BEACON_GUI_TEXTURES); //?
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = 219;
            int j = 0;

            if (!this.enabled) {
                j += this.width * 2;
            } else if (this.selected) {
                j += this.width * 1;
            } else if (this.hovered) {
                j += this.width * 3;
            }

            this.drawTexturedModalRect(this.x, this.y, j, 219, this.width, this.height);

            //if (!GuiBeacon.BEACON_GUI_TEXTURES.equals(this.iconTexture))
            //{
            mc.getTextureManager().bindTexture(this.iconTexture);
            //}

            this.drawTexturedModalRect(this.x + 2, this.y + 2, this.iconX, this.iconY, 18, 18);
        }
    }
}