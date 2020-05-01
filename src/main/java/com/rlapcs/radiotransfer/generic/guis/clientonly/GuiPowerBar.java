package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.capability.MachinePowerHandler;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiPowerBar {
    private CoordinateXY pos;
    private GuiScreen gui;
    private Minecraft mc;
    private MachinePowerHandler powerHandler;

    public GuiPowerBar(int x, int y, MachinePowerHandler powerHandler, Minecraft mc, GuiScreen gui) {
        pos = new CoordinateXY(x, y);
        this.mc = mc;
        this.gui = gui;
    }

    public void draw() {
        mc.getTextureManager().bindTexture(ModConstants.ICONS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        gui.drawTexturedModalRect(pos.x, pos.y, 158, 0, 19, 73);
    }
}
