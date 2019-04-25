package com.rlapcs.radiotransfer.machines.deprecated.transmitter;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractRadioGui;
import net.minecraft.util.ResourceLocation;

public class GuiTransmitter extends AbstractRadioGui {

    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/transmitter.png");

    public GuiTransmitter(TileTransmitter tileEntity, ContainerTransmitter container) {
        super(tileEntity, container, background);

        FREQUENCY_INCREMENT_X = 24;
        FREQUENCY_INCREMENT_Y = 15;

        FREQUENCY_DECREMENT_X = 24;
        FREQUENCY_DECREMENT_Y = 45;
    }
}