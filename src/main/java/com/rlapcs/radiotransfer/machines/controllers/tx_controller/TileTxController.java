package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNodeWithInventory;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractTileController;

public class TileTxController extends AbstractTileController {
    public enum TxMode {
        ROUND_ROBIN,
        SEQUENTIAL
    }

    private TxMode mode = TxMode.SEQUENTIAL;

    public TileTxController() {
        super();
    }

    public void changeMode() {
        if (mode == TxMode.SEQUENTIAL) {
            mode = TxMode.ROUND_ROBIN;
        } else {
            mode = TxMode.SEQUENTIAL;
        }
    }

    public TxMode getMode() {
        return mode;
    }
}
