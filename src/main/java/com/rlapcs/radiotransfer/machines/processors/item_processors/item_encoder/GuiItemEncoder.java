package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractGuiItemProcessor;
import net.minecraft.util.ResourceLocation;

public class GuiItemEncoder extends AbstractGuiItemProcessor {
    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/item_encoder.png");

    public GuiItemEncoder(TileItemEncoder tileEntity, ContainerItemEncoder container, int width, int height, ResourceLocation texture) {
        super(tileEntity, container, width, height, texture);
    }
}
