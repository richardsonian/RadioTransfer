package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.network.messages.MessageChangeTileTxControllerMode;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractGuiController;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiTxController extends AbstractGuiController {

    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/tx_controller.png");

    public GuiTxController(TileTxController tileEntity, ContainerTxController container) {
        super(tileEntity, container, background);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        if (button.id == SECONDARY_INCREMENT_ID) {
            sendChatMessage("mode changed");

            ModNetworkMessages.INSTANCE.sendToServer(new MessageChangeTileTxControllerMode(tileEntity));
            ((TileTxController) tileEntity).changeMode();
        } else if (button.id == SECONDARY_DECREMENT_ID) {
            sendChatMessage("mode changed");

            ModNetworkMessages.INSTANCE.sendToServer(new MessageChangeTileTxControllerMode(tileEntity));
            ((TileTxController) tileEntity).changeMode();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        String mode = ((TileTxController) tileEntity).getMode() == TileTxController.TxMode.ROUND_ROBIN ? "RR" : "S";
        fontRenderer.drawString(mode,  39,  52, Color.white.getRGB());
    }
}
