package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public abstract class InteractiveGuiElement extends GuiButton {
    public InteractiveGuiElement(int id, int x, int y, int iconWidth, int iconHeight) {
        super(id, x, y, iconWidth, iconHeight, "");
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(ModConstants.ICONS);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            this.drawTexturedModalRect(this.x, this.y, getUV().u, getUV().v, this.width, this.height);
        }
    }

    protected abstract CoordinateUV getUV();
}