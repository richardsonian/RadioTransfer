package com.rlapcs.radiotransfer.generic.guis.clientonly;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractMachineGui extends GuiContainer {
    protected ResourceLocation texture;
    protected TileEntity tileEntity;

    private static int ID;

    public AbstractMachineGui(TileEntity tileEntity, Container container, int width, int height, ResourceLocation texture) {
        super(container);

        this.tileEntity = tileEntity;

        xSize = width;
        ySize = height;

        this.texture = texture;
    }

    @Override
    public void initGui() {
        super.initGui();
        //add buttons here
    }

    protected static int createID() {
        return ID++;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
