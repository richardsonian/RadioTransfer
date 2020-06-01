package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.indicators.GuiPowerIndicator;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.AbstractGuiList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.GuiHoverable;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.GuiTooltip;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.TooltipContent;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.HashMap;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;


public abstract class AbstractGuiMachine<T extends TileEntity> extends GuiContainer implements IGui {
    protected ResourceLocation texture;
    protected T tileEntity;
    protected CoordinateXY pos;
    protected DimensionWidthHeight size;
    protected GuiPowerIndicator powerIndicator;
    protected HashMap<String, GuiHoverable> hoverables;
    public GuiTooltip tooltip;

    private static int nextButtonID;

    public AbstractGuiMachine(T tileEntity, Container container) {
        super(container);

        this.tileEntity = tileEntity;
        this.tooltip = new GuiTooltip(new CoordinateXY(Mouse.getX(), Mouse.getY()), new DimensionWidthHeight(4, 4));
        this.hoverables = new HashMap<>();
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
        powerIndicator = new GuiPowerIndicator(this);
        tooltip.setWorldAndResolution(mc, this.width, this.height);

        hoverables.clear();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawRect(0, 0, width, height, 0x90000000);
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(pos.x, pos.y, 0, 0, size.width, size.height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        for (Slot slot : inventorySlots.inventorySlots) {
            String key = "slot_" + slot.hashCode();
            if (slot.getHasStack()) {
                if (hoverables.get(key) == null)
                    hoverables.put(key, new GuiHoverable(new CoordinateXY(pos.x + slot.xPos, pos.y + slot.yPos), new DimensionWidthHeight(16, 16), this));
                hoverables.get(key).check(new TooltipContent(slot.getStack().getTooltip(mc.player, ITooltipFlag.TooltipFlags.NORMAL)));
            } else if (hoverables.get(key) != null)
                hoverables.remove(key);
        }

        if (tileEntity instanceof AbstractTileMultiblockNode && !((AbstractTileMultiblockNode) tileEntity).getClientPowered())
            powerIndicator.draw();
        tooltip.draw();
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
