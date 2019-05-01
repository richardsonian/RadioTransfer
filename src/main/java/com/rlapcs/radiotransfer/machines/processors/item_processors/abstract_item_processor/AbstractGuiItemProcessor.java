package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractGuiItemProcessor<T extends AbstractTileItemProcessor> extends AbstractGuiMachine<T> {
    public AbstractGuiItemProcessor(T tileEntity, AbstractContainerItemProcessor container, int width, int height, ResourceLocation texture) {
        super(tileEntity, container, width, height, texture);
    }

    @Override
    public void initGui() {
        super.initGui();
    }
}
