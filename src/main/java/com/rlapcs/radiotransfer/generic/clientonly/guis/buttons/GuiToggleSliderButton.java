package com.rlapcs.radiotransfer.generic.clientonly.guis.buttons;

import com.rlapcs.radiotransfer.RadioTransfer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;


//not fully implemented
public class GuiToggleSliderButton extends GuiButton {
    private static final ResourceLocation textures = new ResourceLocation(RadioTransfer.MODID, "textures/gui/icons.png");

    public GuiToggleSliderButton(int buttonId, int x, int y) {
        super(buttonId, x, y, 12, 18, "");
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(textures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            this.drawTexturedModalRect(this.x, this.y, 25, 0, this.width, this.height);
        }
    }
}
