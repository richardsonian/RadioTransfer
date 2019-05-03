package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import com.rlapcs.radiotransfer.util.ItemUtils;
import net.minecraft.item.ItemStack;

import static com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractContainerItemProcessor.TILE_SLOTS_START_INDEX;

public class TileItemEncoder extends AbstractTileItemProcessor {
    public static final int INVENTORY_SIZE = 17;
    public static final double POWER_USAGE = 10;

    public TileItemEncoder() {
        super(INVENTORY_SIZE);
    }

    @Override
    protected boolean canDoProcess() {
        return  !ItemUtils.isInventoryEmpty(itemStackHandler, TILE_SLOTS_START_INDEX, itemStackHandler.getSlots());
    }

    @Override
    protected void doProcess() {
        ItemStack stack = ItemUtils.extractNextItems(itemStackHandler, TILE_SLOTS_START_INDEX, itemStackHandler.getSlots(), ITEMS_PER_PROCESS);
        if(!stack.isEmpty()) {
            packetQueue.add(stack);
        }
    }

    @Override
    public double getPowerUsagePerTick() {
        return POWER_USAGE;
    }
    @Override
    public ProcessorType getProcessorType() {return ProcessorType.ENCODER;}
}
