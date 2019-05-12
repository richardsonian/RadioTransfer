package com.rlapcs.radiotransfer.machines.power_supply;

import com.rlapcs.radiotransfer.ModConstants;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import net.minecraftforge.energy.IEnergyStorage;

public class TilePowerSupply extends AbstractTileMultiblockNodeWithInventory {
    public static final int INVENTORY_SIZE = 1;

    public static final int POWER_ITEM_INDEX = 0;

    protected IEnergyStorage energyStorage;

    public TilePowerSupply() {
        super(INVENTORY_SIZE);

        upgradeSlotWhitelists.put(POWER_ITEM_INDEX, ModConstants.UpgradeCards.POWER_ITEM_WHITELIST); //"upgrade card" lol
    }

    public int extractEnergy(int amount, boolean simulate) {
        return 0;//extractEnergy(amount, simulate);
    }

    @Override
    public double getPowerUsagePerTick() {
        return 0;
    }
}
