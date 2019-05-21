package com.rlapcs.radiotransfer.machines.controllers.rx_controller;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageChangeTileRxControllerPriority;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractGuiController;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiRxController extends AbstractGuiController<TileRxController> {

    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/rx_controller.png");

    public GuiRxController(TileRxController tileEntity, ContainerRxController container) {
        super(tileEntity, container, background);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        if (button.id == SECONDARY_INCREMENT_ID) {
            sendChatMessage("priority incremented");

            ModNetworkMessages.INSTANCE.sendToServer(new MessageChangeTileRxControllerPriority(tileEntity, true));
            tileEntity.changePriority(true);
        } else if (button.id == SECONDARY_DECREMENT_ID) {
            sendChatMessage("priority decremented");

            ModNetworkMessages.INSTANCE.sendToServer(new MessageChangeTileRxControllerPriority(tileEntity, false));
            tileEntity.changePriority(false);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        String priority = String.valueOf(tileEntity.getPriority());
        fontRenderer.drawString(priority,  39,  52, Color.white.getRGB());
    }
}
