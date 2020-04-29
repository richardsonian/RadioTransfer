package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import com.rlapcs.radiotransfer.util.Debug;
import com.rlapcs.radiotransfer.util.ItemUtils;
import net.minecraft.item.ItemStack;

public class TileItemDecoder extends AbstractTileItemProcessor {
    public static final int POWER_USAGE = 10;

    public TileItemDecoder() {
        super();
    }

    @Override
    public boolean canDoProcess() {
        boolean superCheck = super.canDoProcess();
        boolean hasPackets = !packetQueue.isEmpty();
        boolean hasSpace = ItemUtils.canMergeAnyIntoInventory(packetQueue.peekNextPacket(ItemPacketQueue.MAX_QUANTITY), itemStackHandler, getNonUpgradeInventorySlots());

        //Debug.sendToAllPlayers(String.format("Decoder hasPackets(%b), hasSpace(%b)", hasPackets, hasSpace), world);
        return superCheck && hasPackets && hasSpace;
    }
    @Override
    public void doProcess() {
        //super.doProcess();
        ItemStack toProcess = packetQueue.getNextPacket(getItemsPerProcess());
        ItemStack remainder = ItemUtils.mergeStackIntoInventory(toProcess, itemStackHandler, getNonUpgradeInventorySlots());
        packetQueue.add(remainder);
        Debug.sendToAllPlayers("Processing packet " + toProcess + " into items in " + this, world);
    }

    @Override
    public ProcessorType getProcessorType() {return ProcessorType.DECODER;}
    @Override
    public int getPowerUsagePerTick() {
        return POWER_USAGE;
    }

}
