package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;

public class TileItemDecoder extends AbstractTileItemProcessor {
    public static final int INVENTORY_SIZE = 17;
    public static final double POWER_USAGE = 10;


    @Override
    public boolean canDoProcess() {
        return false;
    }
    @Override
    public void doProcess() {

    }

    public TileItemDecoder() {
        super(INVENTORY_SIZE);
    }

    @Override
    public ProcessorType getProcessorType() {return ProcessorType.DECODER;}
    @Override
    public double getPowerUsagePerTick() {
        return POWER_USAGE;
    }

}
