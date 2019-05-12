package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiPowerSupply extends AbstractGuiMachine<TilePowerSupply> {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/power_supply.png");

    public GuiPowerSupply(TilePowerSupply tileEntity, ContainerPowerSupply container) {
        super(tileEntity, container, WIDTH, HEIGHT, background);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);


    }
}
