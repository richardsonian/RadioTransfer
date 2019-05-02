package com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder;

import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractContainerItemProcessor;
import net.minecraft.inventory.IInventory;

public class ContainerItemEncoder extends AbstractContainerItemProcessor {
    private static final int[] UPGRADE_POS = {90, 84};
    private static final int[] SLOTS_POS = {6, 24};
    private static final int[] LIST_POS = {};

    public ContainerItemEncoder(IInventory playerInventory, TileItemEncoder te) {
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
