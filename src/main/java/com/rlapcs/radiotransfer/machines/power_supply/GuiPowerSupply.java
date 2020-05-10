package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.GuiPowerBar;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.AbstractGuiList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.PowerSupplyList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiDraggableSliderButton;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.generic.multiblock.MultiblockPowerUsageData;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageAddClientListener;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiPowerSupply extends AbstractGuiMachine<TilePowerSupply> {
    private GuiPowerBar powerBar;
    private PowerSupplyList list;

    private final CoordinateXY LIST_POS = new CoordinateXY(7, 24);
    //private DimensionWidthHeight LIST_SIZE = new DimensionWidthHeight(80, 75);

    public GuiPowerSupply(TilePowerSupply tileEntity, ContainerPowerSupply container) {
        super(tileEntity, container);
        size = new DimensionWidthHeight(188, 197);
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/power_supply.png");
    }

    @Override
    public void initGui() {
        super.initGui();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, true));
        sendDebugMessage("init power gui");
        sendDebugMessage("co" + tileEntity.getController());

        list = new PowerSupplyList(mc, this, tileEntity.getController().getPowerUsageData(), LIST_POS.x, LIST_POS.y, pos.x, pos.y, tileEntity);
        powerBar = new GuiPowerBar(162, 25, tileEntity.getEnergyStorage(), mc,this);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, false));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        list.drawList(mouseX, mouseY, partialTicks, mc.getRenderItem());
        powerBar.draw();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        String power = tileEntity.getDisplayEnergy() + "FE";
        fontRenderer.drawString(power,  22,  30, Color.white.getRGB());
    }
}
