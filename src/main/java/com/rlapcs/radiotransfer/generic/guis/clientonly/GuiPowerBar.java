package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.capability.MachinePowerHandler;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class GuiPowerBar {
    private CoordinateXY pos;
    private GuiScreen gui;
    private Minecraft mc;
    private MachinePowerHandler powerHandler;

    public GuiPowerBar(int x, int y, MachinePowerHandler powerHandler, Minecraft mc, GuiScreen gui) {
        pos = new CoordinateXY(x, y);
        this.mc = mc;
        this.gui = gui;
        this.powerHandler = powerHandler;
    }

    public void draw() {
        double stored = (double) powerHandler.getEnergyStored() / (double) powerHandler.getMaxEnergyStored();
        int barHeight = MathHelper.clamp((int) (71 * stored), 0, 71);

        int green = 0x357F42;
        int r1 = green & 0x000000ff;
        int g1 = green & 0x0000ff00;
        int b1 = green & 0x00ff0000;
        int red = 0x8D1726;
        int r2 = red & 0x000000ff;
        int g2 = red & 0x0000ff00;
        int b2 = red & 0x00ff0000;

        mc.getTextureManager().bindTexture(ModConstants.ICONS);
        GlStateManager.color((float) (r1 + ((r2 - r1) * stored / 256)) / 256, (float) (g1 + ((g2 - g1) * stored / 256)) / 256, (float) (b1 + ((b2 - b1) * stored / 256)) / 256, 1.0F);
        gui.drawTexturedModalRect(pos.x, pos.y + 73 - barHeight, 158, 73 - barHeight, 19, barHeight);
        if (barHeight > 0)
            gui.drawTexturedModalRect(pos.x, pos.y + 71 - barHeight, 158, 0, 19, 2);
    }
}
