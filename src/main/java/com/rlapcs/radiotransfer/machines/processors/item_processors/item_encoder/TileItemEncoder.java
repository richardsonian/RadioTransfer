package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import com.rlapcs.radiotransfer.util.ItemUtils;
import net.minecraft.item.ItemStack;

import static com.rlapcs.radiotransfer.RadioTransfer.sendDebugMessage;

public class TileItemEncoder extends AbstractTileItemProcessor {
    public static final int INVENTORY_SIZE = 17;
    public static final double POWER_USAGE = 10;

    public TileItemEncoder() {
        super(INVENTORY_SIZE);
    }

    @Override
    public boolean canDoProcess() {
        boolean hasItems = !ItemUtils.isInventoryEmpty(itemStackHandler, TILE_ENTITY_START_INDEX, itemStackHandler.getSlots());
        boolean hasSpace = ItemUtils.getFirstIndexInInventoryWhich(itemStackHandler, TILE_SLOTS_START_INDEX, itemStackHandler.getSlots(), (stack) -> packetQueue.canAddAny(stack)) != -1;
        return hasItems && hasSpace;
    }

    @Override
    public void doProcess() {
        int index = ItemUtils.getFirstIndexInInventoryWhich(itemStackHandler, TILE_SLOTS_START_INDEX, itemStackHandler.getSlots(), (stack) -> packetQueue.canAddAny(stack));
        if(index != -1) {
            ItemStack stack = itemStackHandler.getStackInSlot(index);
            if (!stack.isEmpty()) {
                ItemStack remainder = packetQueue.add(stack, getItemsPerProcess());
                itemStackHandler.setStackInSlot(index, remainder);
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
