package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiRadio extends GuiScreen {
    private ResourceLocation texture;
    private TileRadio tileEntity;

    public GuiRadio(TileRadio tileEntity,) {
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/radio.png");
        this.tileEntity = tileEntity;

    }

    @Override
    protected void drawGuiContainterBackgroundLayer(float partialTicks, int mouseX, int mouseY) { // find the equivalent method in GuiScreen, this only exists in GuiContainer
        drawRect(0, 0, width, height, 0x70000000);
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
