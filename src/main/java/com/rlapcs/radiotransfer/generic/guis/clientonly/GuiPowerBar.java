package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.generic.capability.MachinePowerHandler;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiPowerBar {
    private CoordinateXY pos;
    private Minecraft mc;
    private GuiScreen screen;
    private MachinePowerHandler powerHandler;

    public GuiPowerBar(int x, int y, MachinePowerHandler powerHandler, Minecraft minecraft, GuiScreen gui) {
        pos = new CoordinateXY(x, y);
        mc = minecraft;
        screen = gui;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {

    }
}
