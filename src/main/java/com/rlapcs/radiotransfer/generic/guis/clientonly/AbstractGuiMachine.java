package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.AbstractGuiList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;


public abstract class AbstractGuiMachine<T extends TileEntity> extends GuiContainer {
    protected ResourceLocation texture;
    protected T tileEntity;

    protected AbstractGuiList visual;
    protected boolean wasClicking;
    protected boolean isScrolling;
    protected GuiDraggableSliderButton bar;
    protected double scrollPos, scrollVal;

    private static int nextButtonID;

    public AbstractGuiMachine(T tileEntity, Container container, int width, int height, ResourceLocation texture) {
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

    protected void drawList() {
        boolean flag = Mouse.isButtonDown(0);

        if (!wasClicking && flag && bar.isMouseOver())
            isScrolling = true;
        else if (!flag)
            isScrolling = false;

        wasClicking = flag;

        int scroll = Mouse.getEventDWheel();
        boolean up = scroll > 0;
        scrollVal = up ? Math.log(Math.abs(scroll) + 1) : -Math.log(Math.abs(scroll) + 1);
        scrollPos = (bar.getY() - 24) / 59d;
    }

    protected static int getNextButtonID() {
        return nextButtonID++;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
