package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip;

import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

public class GuiHoverable {
    private CoordinateXY pos;
    private DimensionWidthHeight size;
    private AbstractGuiMachine screen;
    private boolean isHovering, wasHovering;

    public GuiHoverable(CoordinateXY pos, DimensionWidthHeight size, AbstractGuiMachine screen) {
        this.pos = pos;
        this.size = size;
        this.screen = screen;
        this.isHovering = false;
        this.wasHovering = false;
    }

    public boolean check(ITooltipContent content) {
        wasHovering = isHovering;
        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        isHovering = Mouse.getX() / scaleFactor >= pos.x && Mouse.getX() / scaleFactor <= pos.x + size.width && screen.height - Mouse.getY() / scaleFactor >= pos.y && screen.height - Mouse.getY() / scaleFactor <= pos.y + size.height;
        if (isHovering)
            screen.tooltip.activate(content);
        else if (wasHovering)
            screen.tooltip.deactivate();
        return isHovering;
    }
}
