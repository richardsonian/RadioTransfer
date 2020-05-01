package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.GuiPowerBar;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.AbstractGuiList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageAddClientListener;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;

public class GuiPowerSupply extends AbstractGuiMachine<TilePowerSupply> {
    public static final int WIDTH = 188;
    public static final int HEIGHT = 197;

    private static final CoordinateXY LIST_POS = new CoordinateXY(0,0);

    protected AbstractGuiList visual;
    private GuiPowerBar powerBar;

    public GuiPowerSupply(TilePowerSupply tileEntity, ContainerPowerSupply container) {
        super(tileEntity, container, WIDTH, HEIGHT);
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/power_supply.png");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    public void initGui() {
        super.initGui();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, true));

        powerBar = new GuiPowerBar(162, 25, tileEntity.getEnergyStorage(), mc,this);
        // ADD ITEMS TO visual
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, false));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        powerBar.draw();
        String power = tileEntity.getDisplayEnergy() + "FE";
        fontRenderer.drawString(power,  22,  30, Color.white.getRGB());
    }
}
