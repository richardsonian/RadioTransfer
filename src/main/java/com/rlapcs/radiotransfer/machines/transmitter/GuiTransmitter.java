package com.rlapcs.radiotransfer.machines.transmitter;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractRadioGui;
import net.minecraft.util.ResourceLocation;

public class GuiTransmitter extends AbstractRadioGui {

    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/transmitter.png");

    public GuiTransmitter(TileTransmitter tileEntity, ContainerTransmitter container) {
        super(tileEntity, container, background);

        FREQUENCY_INCREMENT_ID = createID();
        FREQUENCY_INCREMENT_X = 24;
        FREQUENCY_INCREMENT_Y = 15;

        FREQUENCY_DECREMENT_ID = createID();
        FREQUENCY_DECREMENT_X = 24;
        FREQUENCY_DECREMENT_Y = 45;

        ACTIVATE_ID = createID();
        ACTIVATE_ON_Y = 15;
        ACTIVATE_OFF_X = 78;
        ACTIVATE_OFF_Y = 36;
        ACTIVATE_ON_X = 78;
    }
}