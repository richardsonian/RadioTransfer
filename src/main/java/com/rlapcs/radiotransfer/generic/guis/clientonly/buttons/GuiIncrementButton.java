package com.rlapcs.radiotransfer.generic.guis.clientonly.buttons;

import com.rlapcs.radiotransfer.RadioTransfer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiIncrementButton extends GuiButton {
    public enum IncrementType {
        UP,
        DOWN
    }

    protected IncrementType type;
    protected int u, v;

    private static final ResourceLocation textures = new ResourceLocation(RadioTransfer.MODID, "textures/gui/icons.png");

    public GuiIncrementButton(int buttonId, int x, int y, IncrementType type) {
        super(buttonId, x, y, 25, 15, "");
        if (type == IncrementType.UP) {
            u = 0;
            v = 0;
        } else {
            u = 0;
            v = 15;
        }
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

            this.drawTexturedModalRect(this.x, this.y, u, v, this.width, this.height);
        }
    }
}
