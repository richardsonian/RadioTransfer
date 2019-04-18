package com.rlapcs.radiotransfer.generic.clientonly.guis.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiIncrementButton extends GuiButton {
    protected ResourceLocation buttonTexture;

    public GuiIncrementButton(int buttonId, int x, int y, ResourceLocation buttonTexture) {
        super(buttonId, x, y, 12, 5, "");
        this.buttonTexture = buttonTexture;
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(buttonTexture);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            this.drawTexturedModalRect(this.x, this.y, 0, 0, this.width, this.height);
        }
    }
}
