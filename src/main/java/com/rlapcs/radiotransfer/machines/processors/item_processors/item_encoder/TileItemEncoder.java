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
        boolean hasItems = !ItemUtils.isInventoryEmpty(itemStackHandler, TILE_SLOTS_START_INDEX, itemStackHandler.getSlots());
        boolean canSend = ItemUtils.getFirstIndexInInventoryWhich(itemStackHandler, TILE_SLOTS_START_INDEX, itemStackHandler.getSlots(), (i) -> packetQueue.canAddAny(i)) != -1;
        return  hasItems && canSend;
    }

    @Override
    protected void doProcess() {
        int index = ItemUtils.getFirstIndexInInventoryWhich(itemStackHandler, TILE_SLOTS_START_INDEX, itemStackHandler.getSlots(), (i) -> packetQueue.canAddAny(i));
        if(index != -1) {
            ItemStack stack = itemStackHandler.getStackInSlot(index);
            if (!stack.isEmpty()) {
                ItemStack remainder = packetQueue.add(stack, ITEMS_PER_PROCESS);
                itemStackHandler.setStackInSlot(index, remainder);
                //sendDebugMessage()
            }
        }
    }

    @Override
    public double getPowerUsagePerTick() {
        return POWER_USAGE;
    }
    @Override
    public ProcessorType getProcessorType() {return ProcessorType.ENCODER;}
}
