package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractRadioGui;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons.GuiIncrementButton;
import com.rlapcs.radiotransfer.machines.deprecated.transmitter.ContainerTransmitter;
import com.rlapcs.radiotransfer.machines.deprecated.transmitter.TileTransmitter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiTxController extends AbstractRadioGui {

    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/tx_controller.png");

    protected static int MODE_INCREMENT_ID = getNextButtonID();
    protected static int MODE_INCREMENT_X = 57;
    protected static int MODE_INCREMENT_Y = 51;

    protected static int MODE_DECREMENT_ID = getNextButtonID();
    protected static int MODE_DECREMENT_X = 28;
    protected static int MODE_DECREMENT_Y = 51;

    public GuiTxController(TileTransmitter tileEntity, ContainerTransmitter container) {
        super(tileEntity, container, background);
    }

    @Override
    public void initGui() {
        super.initGui();

        //mode increment button
        this.addButton(new GuiIncrementButton(MODE_INCREMENT_ID, guiLeft + MODE_INCREMENT_X, guiTop + MODE_INCREMENT_Y,
                GuiIncrementButton.IncrementType.RIGHT));

        //mode decrement button
        this.addButton(new GuiIncrementButton(MODE_DECREMENT_ID, guiLeft + MODE_DECREMENT_X, guiTop + MODE_DECREMENT_Y,
                GuiIncrementButton.IncrementType.LEFT));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        if (button.id == MODE_INCREMENT_ID) {
            sendChatMessage("mode changed");

        } else if (button.id == MODE_DECREMENT_ID) {
            sendChatMessage("mode changed");

        }
    }
}
