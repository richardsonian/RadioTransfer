package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.GuiPowerBar;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.GuiTooltip;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.PowerSupplyList;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageAddClientListener;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiPowerSupply extends AbstractGuiMachine<TilePowerSupply> {
    private GuiPowerBar powerBar;
    private PowerSupplyList list;
    private GuiTooltip tooltip;

    private final CoordinateXY LIST_POS = new CoordinateXY(7, 24);
    private final CoordinateXY POWER_BAR_POS = new CoordinateXY(162, 25);
    //private DimensionWidthHeight LIST_SIZE = new DimensionWidthHeight(80, 75);

    public GuiPowerSupply(TilePowerSupply tileEntity, ContainerPowerSupply container) {
        super(tileEntity, container);
        size = new DimensionWidthHeight(188, 197);
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/power_supply.png");
        tooltip = new GuiTooltip(new CoordinateXY(Mouse.getX(), Mouse.getY()), new DimensionWidthHeight(4, 4));
    }

    @Override
    public void initGui() {
        super.initGui();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, true));
        list = new PowerSupplyList(mc, this, tileEntity.getCachedPowerUsageData(), LIST_POS.x, LIST_POS.y, pos.x, pos.y, tileEntity);
        powerBar = new GuiPowerBar((CoordinateXY) POWER_BAR_POS.addTo(pos), tileEntity, this);
        //sendDebugMessage("init power gui");
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
        tooltip.draw();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        String power = String.format("%d/%dFE", tileEntity.getDisplayEnergy(), tileEntity.getMaxEnergy());
        fontRenderer.drawString(power,  150,  30, Color.white.getRGB());
    }

    public GuiTooltip getTooltip() {
        return tooltip;
    }
}
