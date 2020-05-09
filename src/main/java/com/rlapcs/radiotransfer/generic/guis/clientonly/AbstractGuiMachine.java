package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.AbstractGuiList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;


public abstract class AbstractGuiMachine<T extends TileEntity> extends GuiContainer implements IGui {
    protected ResourceLocation texture;
    protected T tileEntity;
    protected CoordinateXY pos;
    protected DimensionWidthHeight size;

    private static int nextButtonID;

    public AbstractGuiMachine(T tileEntity, Container container) {
        super(container);

        this.tileEntity = tileEntity;
    }

    protected static int getNextButtonID() {
        return nextButtonID++;
    }

    @Override
    public void initGui() {
        this.xSize = size.width;
        this.ySize = size.height;
        super.initGui();
        pos = new CoordinateXY(guiLeft, guiTop);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawRect(0, 0, width, height, 0x70000000);
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(pos.x, pos.y, 0, 0, size.width, size.height);
    }

    @Override
    public CoordinateXY getGuiPos() {
        return pos;
    }

    @Override
    public DimensionWidthHeight getGuiSize() {
        return size;
    }
}
