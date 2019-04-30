package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractTileController;
import net.minecraft.nbt.NBTTagCompound;

public class TileTxController extends AbstractTileController {
    public enum TxMode {
        ROUND_ROBIN,
        SEQUENTIAL
    }

    private TxMode mode;

    public TileTxController() {
        super();
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
