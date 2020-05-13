package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

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
    private static final Color FULL = Color.decode("#458F52");
    private static final Color EMPTY = Color.decode("#9D2736");

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
        int ticksSinceLastUpdate = (int) (tile.getTicksSinceCreation() - tickAtLastUpdate);
        if (tile.getTicksSinceCreation() % TilePowerSupply.POWER_BAR_CLIENT_UPDATE_TICKS == 0) {
            tickAtLastUpdate = tile.getTicksSinceCreation();
            prevStoredEnergy = storedEnergy;
            storedEnergy = (double) tile.getDisplayEnergy() / (double) tile.getMaxEnergy();
        }
        //sendDebugMessage("ticks " + ticksSinceLastUpdate);

        double difference = storedEnergy - prevStoredEnergy;
        double last = interpolatedEnergyValue;
        //interpolatedEnergyValue = prevStoredEnergy + difference / TilePowerSupply.POWER_BAR_CLIENT_UPDATE_TICKS * ticksSinceLastUpdate;
        interpolatedEnergyValue += (storedEnergy - interpolatedEnergyValue) / (TilePowerSupply.POWER_BAR_CLIENT_UPDATE_TICKS * 3);
        double interpolation = last + (interpolatedEnergyValue - last) / 4;
        //sendDebugMessage("interp " + interpolatedEnergyValue + " last " + last + " res " + interpolation);

        int barHeight = MathHelper.clamp((int) (71 * interpolatedEnergyValue), 0, 71);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModConstants.ICONS);
        float[] rgb = interpolateColorsToRGB((float) interpolation);
        GlStateManager.color(rgb[0], rgb[1], rgb[2], 1.0f);
        gui.drawTexturedModalRect(pos.x, pos.y + 73 - barHeight, 158, 73 - barHeight, 19, barHeight);
        if (barHeight > 0)
            gui.drawTexturedModalRect(pos.x, pos.y + 71 - barHeight, 158, 0, 19, 2);
        RenderHelper.enableStandardItemLighting();
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
