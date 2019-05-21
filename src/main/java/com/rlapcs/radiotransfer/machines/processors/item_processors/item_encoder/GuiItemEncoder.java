package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.Coordinate;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractGuiItemProcessor;
import net.minecraft.util.ResourceLocation;

public class GuiItemEncoder extends AbstractGuiItemProcessor<TileItemEncoder> {
    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/item_encoder.png");

    public GuiItemEncoder(TileItemEncoder tileEntity, ContainerItemEncoder container) {
        super(tileEntity, container, background);
        LIST_POS = new Coordinate(115, 27);
        PROGRESS_BAR_POS = new Coordinate(93, 55);
    }
}
