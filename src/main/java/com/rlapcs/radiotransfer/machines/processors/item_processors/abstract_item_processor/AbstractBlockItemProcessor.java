package com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor;

import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractBlockProcessor;

public abstract class AbstractBlockItemProcessor extends AbstractBlockProcessor {
    public AbstractBlockItemProcessor(Class<? extends AbstractTileItemProcessor> tileEntityClass) {
        super(tileEntityClass);
    }
}
