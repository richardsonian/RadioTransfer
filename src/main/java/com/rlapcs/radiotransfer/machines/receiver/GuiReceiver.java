package com.rlapcs.radiotransfer.machines.receiver;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractRadioGui;
import com.rlapcs.radiotransfer.generic.guis.clientonly.buttons.GuiIncrementButton;
import com.rlapcs.radiotransfer.generic.network.messages.MessageUpdateTileReceiverPriority;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileRadio;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiReceiver extends AbstractRadioGui {
    private static final int PRIORITY_INCREMENT_ID = getNextButtonID();
    private static final int PRIORITY_INCREMENT_X = 38;
    private static final int PRIORITY_INCREMENT_Y = 13;

    private static final int PRIORITY_DECREMENT_ID = getNextButtonID();
    private static final int PRIORITY_DECREMENT_X = 38;
    private static final int PRIORITY_DECREMENT_Y = 45;

    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/receiver.png");

    public GuiReceiver(TileReceiver tileEntity, ContainerReceiver container) {
        super(tileEntity, container, background);

        FREQUENCY_INCREMENT_X = 12;
        FREQUENCY_INCREMENT_Y = 13;

        FREQUENCY_DECREMENT_X = 12;
        FREQUENCY_DECREMENT_Y = 45;
    }

    @Override
    public void initGui() {
        super.initGui();

        //priority increment button
        this.addButton(new GuiIncrementButton(PRIORITY_INCREMENT_ID, guiLeft + PRIORITY_INCREMENT_X, guiTop + PRIORITY_INCREMENT_Y,
                GuiIncrementButton.IncrementType.UP));

        //priority decrement button
        this.addButton(new GuiIncrementButton(PRIORITY_DECREMENT_ID, guiLeft + PRIORITY_DECREMENT_X, guiTop + PRIORITY_DECREMENT_Y,
                GuiIncrementButton.IncrementType.DOWN));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
        if (button.id == PRIORITY_INCREMENT_ID) {
            sendChatMessage("priority incremented"); //Debug

            ModNetworkMessages.INSTANCE.sendToServer(new MessageUpdateTileReceiverPriority(tileEntity, true));
            ((TileReceiver) tileEntity).changePriority(true);
        }
        if (button.id == PRIORITY_DECREMENT_ID) {
            sendChatMessage("priority decremented"); //debug

            ModNetworkMessages.INSTANCE.sendToServer(new MessageUpdateTileReceiverPriority(tileEntity, false));
            ((TileReceiver) tileEntity).changePriority(false);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        String priority = "" + ((TileReceiver) tileEntity).getPriority();
        fontRenderer.drawString(priority,  PRIORITY_INCREMENT_X + 6,  PRIORITY_DECREMENT_Y - 15, Color.white.getRGB());
    }
}
