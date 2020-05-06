package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.AbstractGuiList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;


public abstract class AbstractGuiMachine<T extends TileEntity> extends GuiContainer {
    protected ResourceLocation texture;
    protected T tileEntity;

    private static int nextButtonID;

    public AbstractGuiMachine(T tileEntity, Container container, int width, int height) {
        super(container);

        this.tileEntity = tileEntity;
        xSize = width;
        ySize = height;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    protected static int getNextButtonID() {
        return nextButtonID++;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawRect(0, 0, width, height, 0x70000000);
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
