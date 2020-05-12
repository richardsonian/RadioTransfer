package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiPowerBar {
    private CoordinateXY pos;
    private GuiScreen gui;
    private TilePowerSupply tile;
    private double storedEnergy;
    private double prevStoredEnergy;
    private double interpolatedEnergyValue;
    private long tickAtLastUpdate;

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
        interpolatedEnergyValue = prevStoredEnergy + difference / TilePowerSupply.POWER_BAR_CLIENT_UPDATE_TICKS * ticksSinceLastUpdate;
        double interpolation = last + (interpolatedEnergyValue - last) / 4;
        //sendDebugMessage("interp " + interpolatedEnergyValue + " last " + last + " res " + interpolation);

        int barHeight = MathHelper.clamp((int) (71 * interpolation), 0, 71);
        int green = 0x357F42;
        int red = 0x8D1726;

        int r1 = green & 0x0000ff;
        int g1 = green & 0x00ff00;
        int b1 = green & 0xff0000;
        int r2 = red & 0x0000ff;
        int g2 = red & 0x00ff00;
        int b2 = red & 0xff0000;

        Minecraft.getMinecraft().getTextureManager().bindTexture(ModConstants.ICONS);
        GlStateManager.color((float) (r1 + ((r2 - r1) * interpolation / 256)) / 256, (float) (g1 + ((g2 - g1) * interpolation / 256)) / 256, (float) (b1 + ((b2 - b1) * interpolation / 256)) / 256, 1.0F);
        gui.drawTexturedModalRect(pos.x, pos.y + 73 - barHeight, 158, 73 - barHeight, 19, barHeight);
        if (barHeight > 0)
            gui.drawTexturedModalRect(pos.x, pos.y + 71 - barHeight, 158, 0, 19, 2);
    }
}
