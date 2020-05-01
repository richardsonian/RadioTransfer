package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiRadio extends AbstractGuiMachine<TileRadio> {
    public GuiRadio(TileRadio tileEntity, Container container, int width, int height) {
        super(tileEntity, container, width, height);
        texture = new ResourceLocation(RadioTransfer.MODID, "textures/gui/radio.png");
    }
}
