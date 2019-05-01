package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;

public class TileItemEncoder extends AbstractTileItemProcessor {
    public static final int INVENTORY_SIZE = 12;
    public static final double POWER_USAGE = 10;

    public TileItemEncoder() {
        super(INVENTORY_SIZE);
    }

    @Override
    public double getPowerUsagePerTick() {
        return POWER_USAGE;
    }

    @Override
    public ProcessorType getProcessorType() {return ProcessorType.ENCODER;}
}
