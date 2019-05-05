package com.rlapcs.radiotransfer.machines.processors.item_processors.item_decoder;

import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractContainerItemProcessor;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.TileItemEncoder;
import net.minecraft.inventory.IInventory;

public class ContainerItemDecoder extends AbstractContainerItemProcessor {
    private static final int[] UPGRADE_POS = {90, 84}; //needs to be changed
    private static final int[] SLOTS_POS = {6, 24}; //needs to be changed
    private static final int[] LIST_POS = {};

    public ContainerItemDecoder(IInventory playerInventory, TileItemDecoder te) {
        super(playerInventory, te);
    }

    @Override
    public int[] getUpgradePos() {
        return UPGRADE_POS;
    }

    @Override
    public int[] getSlotsPos() {
        return SLOTS_POS;
    }
}
