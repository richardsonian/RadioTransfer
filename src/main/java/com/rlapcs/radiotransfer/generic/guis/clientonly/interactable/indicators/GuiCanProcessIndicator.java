package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.indicators;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

public class GuiCanProcessIndicator {
    private static final DimensionWidthHeight size = new DimensionWidthHeight(9, 9);
    private static final CoordinateUV uv = new CoordinateUV(8, 21);
    private AbstractGuiMachine screen;
    private CoordinateXY pos;

    public GuiCanProcessIndicator(AbstractGuiMachine screen, CoordinateXY pos) {
        this.screen = screen;
        this.pos = pos;
    }

    public void draw() {
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModConstants.ICONS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        screen.drawTexturedModalRect(pos.x, pos.y, uv.u, uv.v, size.width, size.height);
        RenderHelper.enableStandardItemLighting();
    }
}
