package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.indicators;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.TooltipContent;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.machines.power_supply.GuiPowerSupply;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.input.Mouse;

public class GuiPowerIndicator {
    private static final DimensionWidthHeight size = new DimensionWidthHeight(16, 16);
    private static final CoordinateUV white_back = new CoordinateUV(92, 20);
    private static final CoordinateUV red_back = new CoordinateUV(108, 20);
    private AbstractGuiMachine screen;
    private CoordinateXY pos;
    private int counter;
    private boolean wasHovering, isHovering;

    public GuiPowerIndicator(AbstractGuiMachine screen) {
        this.screen = screen;
        this.pos = new CoordinateXY(screen.getGuiPos().addTo(screen.getGuiSize()).x + 2, screen.getGuiPos().y + 18); // height of header - standardize!
        this.counter = 0;
        this.wasHovering = false;
        this.isHovering = false;
    }

    public void draw() {
        counter++;
        counter = counter % 30;

        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModConstants.ICONS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (counter < 15)
            screen.drawTexturedModalRect(pos.x, pos.y, white_back.u, white_back.v, size.width, size.height);
        else
            screen.drawTexturedModalRect(pos.x, pos.y, red_back.u, red_back.v, size.width, size.height);
        RenderHelper.enableStandardItemLighting();

        wasHovering = isHovering;
        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        isHovering = Mouse.getX() / scaleFactor >= pos.x && screen.height - Mouse.getY() / scaleFactor >= pos.y && Mouse.getX() / scaleFactor < pos.x + size.width && screen.height - Mouse.getY() / scaleFactor < pos.y + size.height;
        if (isHovering)
            screen.tooltip.activate(new TooltipContent("Insufficient power."));
        else if (wasHovering)
            screen.tooltip.deactivate();
    }
}
