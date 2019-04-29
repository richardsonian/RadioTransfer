package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons.GuiIncrementButton;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiToggleSliderButton;
import com.rlapcs.radiotransfer.generic.network.messages.MessageChangeTileTxControllerMode;
import com.rlapcs.radiotransfer.machines._deprecated.other.AbstractTileRadio;
import com.rlapcs.radiotransfer.machines._deprecated.other.MessageActivateTileRadio;
import com.rlapcs.radiotransfer.machines._deprecated.other.MessageUpdateTileRadioFrequency;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractGuiController;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractTileController;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiTxController extends AbstractGuiController {

    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/tx_controller.png");

    protected static int MODE_INCREMENT_ID = getNextButtonID();
    protected static int MODE_INCREMENT_X = 57;
    protected static int MODE_INCREMENT_Y = 51;

    protected static int MODE_DECREMENT_ID = getNextButtonID();
    protected static int MODE_DECREMENT_X = 28;
    protected static int MODE_DECREMENT_Y = 51;

    public GuiTxController(TileTxController tileEntity, ContainerTxController container) {
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

            ModNetworkMessages.INSTANCE.sendToServer(new MessageChangeTileTxControllerMode(tileEntity));
            ((TileTxController) tileEntity).changeMode();
        } else if (button.id == MODE_DECREMENT_ID) {
            sendChatMessage("mode changed");

        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        String mode = ((TileTxController) tileEntity).getMode() == TileTxController.TxMode.ROUND_ROBIN ? "RR" : "S";
        fontRenderer.drawString(mode,  39,  52, Color.white.getRGB());
    }
}
