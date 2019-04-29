package com.rlapcs.radiotransfer.machines.controllers.abstract_controller;

import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractMachineGui;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons.GuiIncrementButton;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiToggleSliderButton;
<<<<<<< HEAD
import com.rlapcs.radiotransfer.machines._deprecated.other.MessageActivateTileRadio;
import com.rlapcs.radiotransfer.machines._deprecated.other.MessageUpdateTileRadioFrequency;
=======
import com.rlapcs.radiotransfer.machines._deprecated.other.AbstractTileRadio;
import com.rlapcs.radiotransfer.machines._deprecated.other.messages.MessageActivateTileRadio;
import com.rlapcs.radiotransfer.machines._deprecated.other.messages.MessageUpdateTileRadioFrequency;
>>>>>>> master
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public abstract class AbstractGuiController extends AbstractMachineGui {
    public static final int WIDTH = 170;
    public static final int HEIGHT = 148;

    protected static int FREQUENCY_INCREMENT_ID = getNextButtonID();
    protected static int FREQUENCY_INCREMENT_X = 74;
    protected static int FREQUENCY_INCREMENT_Y = 30;

    protected static int FREQUENCY_DECREMENT_ID = getNextButtonID();
    protected static int FREQUENCY_DECREMENT_X = 13;
    protected static int FREQUENCY_DECREMENT_Y = 30;

    protected static int ACTIVATE_ID = getNextButtonID();
    protected static int ACTIVATE_ON_X = 149;
    protected static int ACTIVATE_ON_Y = 22;
    protected static int ACTIVATE_OFF_X = 149;
    protected static int ACTIVATE_OFF_Y = 43;

    public AbstractGuiController(AbstractTileController tileEntity, AbstractContainerController container, ResourceLocation background) {
        super(tileEntity, container, WIDTH, HEIGHT, background);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == FREQUENCY_INCREMENT_ID) {
            sendChatMessage("frequency incremented"); //Debug

            ModNetworkMessages.INSTANCE.sendToServer(new MessageUpdateTileRadioFrequency(tileEntity, true));
            ((AbstractTileController) tileEntity).changeFrequency(true);
        }
        if(button.id == FREQUENCY_DECREMENT_ID) {
            sendChatMessage("frequency decremented"); //debug

            ModNetworkMessages.INSTANCE.sendToServer(new MessageUpdateTileRadioFrequency(tileEntity, false));
            ((AbstractTileController) tileEntity).changeFrequency(false);
        }
        if(button.id == ACTIVATE_ID) {
            sendChatMessage("activate button pressed"); //DEBUG

            GuiToggleSliderButton activateButton = (GuiToggleSliderButton) button;
            int pos = activateButton.flipState();

            //update server tileEntity
            ModNetworkMessages.INSTANCE.sendToServer(new MessageActivateTileRadio(tileEntity, pos == 1));
            //update client tileEntity
            ((AbstractTileController) tileEntity).setActivated(pos == 1);
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        //frequency increment button
        this.addButton(new GuiIncrementButton(FREQUENCY_INCREMENT_ID, guiLeft + FREQUENCY_INCREMENT_X, guiTop + FREQUENCY_INCREMENT_Y,
                GuiIncrementButton.IncrementType.RIGHT));

        //frequency decrement button
        this.addButton(new GuiIncrementButton(FREQUENCY_DECREMENT_ID, guiLeft + FREQUENCY_DECREMENT_X, guiTop + FREQUENCY_DECREMENT_Y,
                GuiIncrementButton.IncrementType.LEFT));

        //activate button
        this.addButton(new GuiToggleSliderButton(ACTIVATE_ID, ((AbstractTileController) tileEntity).getActivated() ? 1 : 2, guiLeft + ACTIVATE_ON_X, guiTop + ACTIVATE_ON_Y,
                guiLeft + ACTIVATE_OFF_X, guiTop + ACTIVATE_OFF_Y));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY); //draws background
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        //draw text
        //String name = Minecraft.getMinecraft().world.getBlockState(tileEntity.getPos()).getBlock().getLocalizedName();
        //fontRenderer.drawString(name, 5, 5, Color.white.getRGB());

        //fontRenderer.drawString("on", 64, 18, Color.white.getRGB());
        //fontRenderer.drawString("off", 60, 44, Color.white.getRGB());

        String frequency = ((AbstractTileController) tileEntity).getFrequency() + "mHz";
        fontRenderer.drawString(frequency,  22,  30, Color.white.getRGB());
    }

}