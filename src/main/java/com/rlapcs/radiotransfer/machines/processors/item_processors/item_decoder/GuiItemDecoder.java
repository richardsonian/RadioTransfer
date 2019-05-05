package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractGuiItemProcessor;
import net.minecraft.util.ResourceLocation;

public class GuiItemDecoder extends AbstractGuiItemProcessor<TileItemDecoder> {
    public static final ResourceLocation background = new ResourceLocation(RadioTransfer.MODID, "textures/gui/item_decoder.png");

    public GuiItemDecoder(TileItemDecoder tileEntity, ContainerItemDecoder container) {
        super(tileEntity, container, background);
    }

    @Override
    protected int[] getProgressBarCoords() {
        return new int[] {86, 56};
    }
}