package com.rlapcs.radiotransfer.machines.antennas.abstract_antenna;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import net.minecraft.item.Item;

import java.util.Collections;
import java.util.Map;

public class AbstractTileAntenna extends AbstractTileMultiblockNode {
    //does this need to have a tileEntity?

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~POWER USAGE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    // No intention for the Antenna to require any power to run atm, but these fields are required as an AbstractTileMultiblockNode

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public int getBasePowerPerTick() {
        return 0;
    }

    @Override
    public Map<Item, Integer> getUpgradeCardConstantPowerCosts() {
        return Collections.emptyMap();
    }

    @Override
    public int getBasePowerPerProcess() {
        return 0;
    }

    @Override
    public Map<Item, Integer> getUpgradeCardProcessPowerCosts() {
        return Collections.emptyMap();
    }

    @Override
    public Map<Item, Integer> getUpgradeCardQuantities() {
        return Collections.emptyMap();
    }

    @Override
    public double getAverageProcessesRate() {
        return -1; //does not do processes
    }
}
