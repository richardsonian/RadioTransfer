package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.TooltipContent;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.machines.power_supply.GuiPowerSupply;
import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.awt.*;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiPowerBar {
    private CoordinateXY pos;
    private GuiScreen gui;
    private TilePowerSupply tile;
    private double storedEnergy;
    private double prevStoredEnergy;
    private double interpolatedEnergyValue;
    private long tickAtLastUpdate;
    private boolean isHovering, wasHovering;
    private static final Color FULL = Color.decode("#458F52");
    private static final Color EMPTY = Color.decode("#9D2736");
    private static final DimensionWidthHeight size = new DimensionWidthHeight(19, 73);

    public GuiPowerBar(CoordinateXY pos, TilePowerSupply tile, GuiScreen gui) {
        this.pos = pos;
        this.gui = gui;
        this.tile = tile;
        this.storedEnergy = (double) tile.getDisplayEnergy() / (double) tile.getMaxEnergy();
        this.prevStoredEnergy = storedEnergy;
        this.interpolatedEnergyValue = storedEnergy;
        this.tickAtLastUpdate = tile.getTicksSinceCreation();
    }

    public void draw() {
        if (tile.getTicksSinceCreation() % TilePowerSupply.POWER_BAR_CLIENT_UPDATE_TICKS == 0) {
            tickAtLastUpdate = tile.getTicksSinceCreation();
            prevStoredEnergy = storedEnergy;
            storedEnergy = (double) tile.getDisplayEnergy() / (double) tile.getMaxEnergy();
        }

        double last = interpolatedEnergyValue;
        interpolatedEnergyValue += (storedEnergy - interpolatedEnergyValue) / (TilePowerSupply.POWER_BAR_CLIENT_UPDATE_TICKS * 3);
        double interpolation = last + (interpolatedEnergyValue - last) / 4;

        int barHeight = MathHelper.clamp((int) ((size.height - 2) * interpolatedEnergyValue), 0, 71);

        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModConstants.ICONS);
        float[] rgb = interpolateColorsToRGB((float) interpolation);
        GlStateManager.color(rgb[0], rgb[1], rgb[2], 1.0f);
        gui.drawTexturedModalRect(pos.x, pos.y + size.height - barHeight, 158, size.height - barHeight, size.width, barHeight);
        if (barHeight > 0)
            gui.drawTexturedModalRect(pos.x, pos.y + size.height - 2 - barHeight, 158, 0, size.width, 2);
        RenderHelper.enableStandardItemLighting();

        wasHovering = isHovering;
        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        isHovering = Mouse.getX() / scaleFactor >= pos.x && gui.height - Mouse.getY() / scaleFactor >= pos.y && Mouse.getX() / scaleFactor < pos.x + size.width && gui.height - Mouse.getY() / scaleFactor < pos.y + size.height;
        if (isHovering)
            ((GuiPowerSupply) gui).tooltip.activate(new TooltipContent(String.format("%d / %d FE", tile.getDisplayEnergy(), tile.getMaxEnergy())));
        else if (wasHovering)
            ((GuiPowerSupply) gui).tooltip.deactivate();
    }

    private float[] interpolateColorsToRGB(float interpolation) {
        float[] rgb = new float[3];
        float inverse = 1 - interpolation;
        rgb[0] = (FULL.getRed() * interpolation + EMPTY.getRed() * inverse) / 255f;
        rgb[1] = (FULL.getGreen() * interpolation + EMPTY.getGreen() * inverse) / 255f;
        rgb[2] = (FULL.getBlue() * interpolation + EMPTY.getBlue() * inverse) / 255f;
        return rgb;
    }
}
