package com.rlapcs.radiotransfer.machines.controllers.abstract_controller;

import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons.GuiIncrementButton;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.sliders.GuiToggleSliderButton;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageActivateTileController;
import com.rlapcs.radiotransfer.network.messages.toServer.MessageUpdateTileControllerFrequency;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public abstract class AbstractGuiController<T extends AbstractTileController> extends AbstractGuiMachine<T> {
    protected static int FREQUENCY_INCREMENT_ID = getNextButtonID();
    protected static int FREQUENCY_INCREMENT_X = 73;
    protected static int FREQUENCY_INCREMENT_Y = 30;

    protected static int FREQUENCY_DECREMENT_ID = getNextButtonID();
    protected static int FREQUENCY_DECREMENT_X = 11;
    protected static int FREQUENCY_DECREMENT_Y = 30;

    protected static int SECONDARY_INCREMENT_ID = getNextButtonID();
    protected static int SECONDARY_INCREMENT_X = 57;
    protected static int SECONDARY_INCREMENT_Y = 51;

    protected static int SECONDARY_DECREMENT_ID = getNextButtonID();
    protected static int SECONDARY_DECREMENT_X = 27;
    protected static int SECONDARY_DECREMENT_Y = 51;

    protected static int ACTIVATE_ID = getNextButtonID();
    protected static int ACTIVATE_ON_X = 166;
    protected static int ACTIVATE_ON_Y = 22;
    protected static int ACTIVATE_OFF_X = 166;
    protected static int ACTIVATE_OFF_Y = 43;

    public AbstractGuiController(T tileEntity, AbstractContainerController container) {
        super(tileEntity, container);
        size = new DimensionWidthHeight(188, 158);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == FREQUENCY_INCREMENT_ID) {
            sendChatMessage("frequency incremented"); //Debug

            ModNetworkMessages.INSTANCE.sendToServer(new MessageUpdateTileControllerFrequency(tileEntity, true));
            tileEntity.changeFrequency(true);
        }
        if(button.id == FREQUENCY_DECREMENT_ID) {
            sendChatMessage("frequency decremented"); //debug

            ModNetworkMessages.INSTANCE.sendToServer(new MessageUpdateTileControllerFrequency(tileEntity, false));
            tileEntity.changeFrequency(false);
        }
        if(button.id == ACTIVATE_ID) {
            sendChatMessage("activate button pressed"); //DEBUG

            GuiToggleSliderButton activateButton = (GuiToggleSliderButton) button;
            int pos = activateButton.flipState();

            //update server tileEntity
            ModNetworkMessages.INSTANCE.sendToServer(new MessageActivateTileController(tileEntity, pos == 1));
            //update client tileEntity
            tileEntity.setActivated(pos == 1);
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

        //secondary increment button
        this.addButton(new GuiIncrementButton(SECONDARY_INCREMENT_ID, guiLeft + SECONDARY_INCREMENT_X, guiTop + SECONDARY_INCREMENT_Y,
                GuiIncrementButton.IncrementType.RIGHT));

        //secondary decrement button
        this.addButton(new GuiIncrementButton(SECONDARY_DECREMENT_ID, guiLeft + SECONDARY_DECREMENT_X, guiTop + SECONDARY_DECREMENT_Y,
                GuiIncrementButton.IncrementType.LEFT));

        //activate button
        this.addButton(new GuiToggleSliderButton(ACTIVATE_ID, tileEntity.getActivated() ? 1 : 2, guiLeft + ACTIVATE_ON_X, guiTop + ACTIVATE_ON_Y,
                guiLeft + ACTIVATE_OFF_X, guiTop + ACTIVATE_OFF_Y));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        //draw text
        //String name = Minecraft.getMinecraft().world.getBlockState(tileEntity.getPos()).getBlock().getLocalizedName();
        //fontRenderer.drawString(name, 5, 5, Color.white.getRGB());

        //fontRenderer.drawString("on", 64, 18, Color.white.getRGB());
        //fontRenderer.drawString("off", 60, 44, Color.white.getRGB());

        String frequency = tileEntity.getFrequency() + "MHz";
        fontRenderer.drawString(frequency,  22,  30, Color.white.getRGB());
    }

}