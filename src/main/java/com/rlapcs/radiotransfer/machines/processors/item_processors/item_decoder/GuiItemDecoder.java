package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractGuiItemProcessor;
import net.minecraft.util.ResourceLocation;

public class GuiItemDecoder extends AbstractGuiItemProcessor<TileItemDecoder> {
    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/item_decoder.png");

    public GuiItemDecoder(TileItemDecoder tileEntity, ContainerItemDecoder container) {
        super(tileEntity, container, background);

        LIST_POS = new CoordinateXY(8, 27);
        PROGRESS_BAR_POS = new CoordinateXY(86, 56);
    }
}