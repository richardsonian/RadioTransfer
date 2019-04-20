package com.rlapcs.radiotransfer.generic.clientonly.guis.buttons;

import com.rlapcs.radiotransfer.RadioTransfer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;


//not fully implemented
public class GuiToggleSliderButton extends GuiButton {
    private int pos1X, pos1Y, pos2X, pos2Y;

    private int pos; //1 or 2

    private static final ResourceLocation textures = new ResourceLocation(RadioTransfer.MODID, "textures/gui/icons.png");

    public GuiToggleSliderButton(int buttonId, int x, int y) {
        super(buttonId, x, y, 12, 18, "");
    public GuiToggleSliderButton(int buttonId, int startPos, int pos1X, int pos1Y, int pos2X, int pos2Y, int buttonWidth, int buttonHeight, ResourceLocation buttonTexture) {
        super(buttonId, (startPos == 1) ? pos1X : pos2X, (startPos == 1) ? pos1Y : pos2Y, buttonWidth, buttonHeight, "");
        this.buttonTexture = buttonTexture;
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

    public int flipState() {
        if(pos == 1) {
            x = pos2X;
            y = pos2Y;
            pos = 2;
        }
        else {
            x = pos1X;
            y = pos1X;
            pos = 1;
        }
        return pos;
    }
}
