package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.generic.guis.clientonly.AbstractGuiMachine;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractGuiItemProcessor<T extends AbstractTileItemProcessor> extends AbstractGuiMachine<T> {
    private static final int WIDTH = 188;
    private static final int HEIGHT = 197;

    public AbstractGuiItemProcessor(T tileEntity, AbstractContainerItemProcessor container, ResourceLocation texture) {
        super(tileEntity, container, WIDTH, HEIGHT, texture);
    }

    @Override
    public void initGui() {
        super.initGui();
    }
}
