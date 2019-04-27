package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;

public class TileRadio extends AbstractTileMachine {
    public final int MULTIBLOCK_UPDATE_TICKS = 20;

    private int SEND_RESOURCES_UPDATE_TICKS = 20;

    private MultiblockRadioController multiblock;

    private void sendResources() {

    }

    public MultiblockRadioController getMultiblockController() {
        return multiblock;
    }


    @Override
    public void update() {
        super.update();
        if(!world.isRemote) {
            if (ticksSinceCreation % MULTIBLOCK_UPDATE_TICKS == 0) {
                multiblock.validateCurrentMultiblockNodes();
            }

            if(ticksSinceCreation % SEND_RESOURCES_UPDATE_TICKS == 0) {
                sendResources();
            }
        }
    }
}
