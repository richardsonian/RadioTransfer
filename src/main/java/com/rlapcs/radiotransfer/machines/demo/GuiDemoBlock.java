package com.rlapcs.radiotransfer.machines.demo;

import com.rlapcs.radiotransfer.RadioTransfer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiDemoBlock extends GuiContainer {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures" +
            "/gui/mcjty copy.png");

    public GuiDemoBlock(TileDemoBlock tileEntity, ContainerDemoBlock container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}