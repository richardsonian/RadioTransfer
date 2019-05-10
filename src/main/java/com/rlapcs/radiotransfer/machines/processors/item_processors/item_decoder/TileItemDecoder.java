package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import com.rlapcs.radiotransfer.util.ItemUtils;
import net.minecraft.item.ItemStack;

public class TileItemDecoder extends AbstractTileItemProcessor {
    public static final double POWER_USAGE = 10;

    public TileItemDecoder() {
        super();
    }

    @Override
    public boolean canDoProcess() {
        boolean hasPackets = !packetQueue.isEmpty();
        boolean hasSpace = ItemUtils.canMergeAnyIntoInventory(packetQueue.peekNextPacket(ItemPacketQueue.MAX_QUANTITY), itemStackHandler, getNonUpgradeInventorySlots());

        return hasPackets && hasSpace;
    }
    @Override
    public void doProcess() {
        ItemStack toProcess = packetQueue.getNextPacket(getItemsPerProcess());
        ItemStack remainder = ItemUtils.mergeStackIntoInventory(toProcess, itemStackHandler, getNonUpgradeInventorySlots());
        packetQueue.add(remainder);
    }

    @Override
    public ProcessorType getProcessorType() {return ProcessorType.DECODER;}
    @Override
    public double getPowerUsagePerTick() {
        return POWER_USAGE;
    }

}
