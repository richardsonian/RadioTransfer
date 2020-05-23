package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.GuiPowerBar;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.GuiTooltip;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.PowerSupplyList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.PowerSupplyEntryTooltip;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageAddClientListener;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class GuiPowerSupply extends AbstractGuiMachine<TilePowerSupply> {
    private GuiPowerBar powerBar;
    private PowerSupplyList list;
    public GuiTooltip tooltip;
    public PowerSupplyEntryTooltip powerSupplyEntryTooltip;

    private static final CoordinateXY IN_OUT_POS = new CoordinateXY(95, 30);
    private static final CoordinateXY LIST_POS = new CoordinateXY(8, 27);
    private static final DimensionWidthHeight LIST_SIZE = new DimensionWidthHeight(78, 73);
    private static final CoordinateXY POWER_BAR_POS = new CoordinateXY(162, 25);

    public GuiPowerSupply(TilePowerSupply tileEntity, ContainerPowerSupply container) {
        super(tileEntity, container);
        size = new DimensionWidthHeight(188, 197);
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/power_supply.png");
        tooltip = new GuiTooltip(new CoordinateXY(Mouse.getX(), Mouse.getY()), new DimensionWidthHeight(4, 4));
        powerSupplyEntryTooltip = new PowerSupplyEntryTooltip(new CoordinateXY(Mouse.getX(), Mouse.getY()), new DimensionWidthHeight(4, 4));
    }

    @Override
    public void initGui() {
        super.initGui();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, true));
        list = new PowerSupplyList(LIST_POS, LIST_SIZE, this, tileEntity.getCachedPowerUsageData(), pos.x, pos.y, tileEntity);
        powerBar = new GuiPowerBar(POWER_BAR_POS.addTo(pos), tileEntity, this);
        tooltip.setWorldAndResolution(mc, this.width, this.height);
        powerSupplyEntryTooltip.setWorldAndResolution(mc, this.width, this.height);
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
        powerBar.draw();
        list.drawList(mouseX, mouseY, partialTicks, mc.getRenderItem()); //changed this to be drawn after powerbar for debug, is that ok?
        tooltip.draw();
        powerSupplyEntryTooltip.draw();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawString(mc.fontRenderer, "IN", IN_OUT_POS.x, IN_OUT_POS.y, Color.LIGHT_GRAY.getRGB());
        drawString(mc.fontRenderer, "  ", IN_OUT_POS.x, IN_OUT_POS.y + 12, Color.WHITE.getRGB());
        drawString(mc.fontRenderer, "OUT", IN_OUT_POS.x, IN_OUT_POS.y + 24, Color.LIGHT_GRAY.getRGB());
        drawString(mc.fontRenderer, "  " + tileEntity.cachedPowerUsageData.getTotalPowerUsage() + " FE/t", IN_OUT_POS.x, IN_OUT_POS.y + 36, Color.WHITE.getRGB());
    }
}
