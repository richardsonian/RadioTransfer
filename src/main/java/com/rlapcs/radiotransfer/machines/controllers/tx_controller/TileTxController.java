package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractTileController;
import com.rlapcs.radiotransfer.server.radio.TxMode;
import com.rlapcs.radiotransfer.ModConstants;
import net.minecraft.nbt.NBTTagCompound;

public class TileTxController extends AbstractTileController {
    public static final int STACK_UPGRADE_SLOT_INDEX = 1;
    public static final int SPEED_UPGRADE_SLOT_INDEX = 2;

    public static final int INVENTORY_SIZE = ABSTRACT_INVENTORY_SIZE + 2;

    private TxMode mode;

    public TileTxController() {
        super(INVENTORY_SIZE);

        upgradeSlotWhitelists.put(STACK_UPGRADE_SLOT_INDEX, ModConstants.UpgradeCards.STACK_UPGRADE_WHITELIST);
        upgradeSlotWhitelists.put(SPEED_UPGRADE_SLOT_INDEX, ModConstants.UpgradeCards.SPEED_UPGRADE_WHITELIST);

        mode = TxMode.ROUND_ROBIN;
    }

    public void changeMode() {
        if (mode == TxMode.SEQUENTIAL) {
            mode = TxMode.ROUND_ROBIN;
        } else {
            mode = TxMode.SEQUENTIAL;
        }
        this.markDirty();
    }
    public TxMode getMode() {
        return mode;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("roundRobin")) {
            mode = (compound.getBoolean("roundRobin") ? TxMode.ROUND_ROBIN : TxMode.SEQUENTIAL);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setBoolean("roundRobin", (mode == TxMode.ROUND_ROBIN));

        return compound;
    }
}
