package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.GuiPowerBar;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.GuiTooltip;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.PowerSupplyList;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.RadioEntryTooltip;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageAddClientListener;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class GuiPowerSupply extends AbstractGuiMachine<TilePowerSupply> {
    private GuiPowerBar powerBar;
    private PowerSupplyList list;
    public GuiTooltip tooltip;
    public RadioEntryTooltip radioEntryTooltip;

    private final CoordinateXY LIST_POS = new CoordinateXY(8, 27);
    private final CoordinateXY POWER_BAR_POS = new CoordinateXY(162, 25);
    //private DimensionWidthHeight LIST_SIZE = new DimensionWidthHeight(80, 75);

    public GuiPowerSupply(TilePowerSupply tileEntity, ContainerPowerSupply container) {
        super(tileEntity, container);
        size = new DimensionWidthHeight(188, 197);
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/power_supply.png");
        tooltip = new GuiTooltip(new CoordinateXY(Mouse.getX(), Mouse.getY()), new DimensionWidthHeight(4, 4));
        radioEntryTooltip = new RadioEntryTooltip(new CoordinateXY(Mouse.getX(), Mouse.getY()), new DimensionWidthHeight(4, 4));
    }

    @Override
    public void initGui() {
        super.initGui();
        ModNetworkMessages.INSTANCE.sendToServer(new MessageAddClientListener(tileEntity, true));
        list = new PowerSupplyList(mc, this, tileEntity.getCachedPowerUsageData(), LIST_POS.x, LIST_POS.y, pos.x, pos.y, tileEntity);
        powerBar = new GuiPowerBar((CoordinateXY) POWER_BAR_POS.addTo(pos), tileEntity, this);
        tooltip.setWorldAndResolution(mc, this.width, this.height);
        radioEntryTooltip.setWorldAndResolution(mc, this.width, this.height);
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
        GlStateManager.pushMatrix();
        GlStateManager.translate(0f, 0f, 500);
        tooltip.draw();
        radioEntryTooltip.draw();
        GlStateManager.popMatrix();
    }
}
