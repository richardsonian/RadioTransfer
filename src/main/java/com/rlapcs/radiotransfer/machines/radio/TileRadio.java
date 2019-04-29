package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;

public class TileRadio extends AbstractTileMachine {
    public final int MULTIBLOCK_UPDATE_TICKS = 20;
    private int SEND_RESOURCES_UPDATE_TICKS = 20;

    private MultiblockRadioController multiblock;

    public TileRadio() {
        super();
        multiblock = new MultiblockRadioController(this);
    }

    private void sendResources() {
    }

    public MultiblockRadioController getMultiblockController() {
        return multiblock;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if(!world.isRemote) {
            multiblock.deregisterAllNodes();
        }
    }

    /**
     * Called when the chunk this TileEntity is on is Unloaded.
     */
    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        if(!world.isRemote) {
            multiblock.deregisterAllNodes();
        }
    }

    @Override
    public void update() {
        super.update();
        if(!world.isRemote) {
            if (ticksSinceCreation % MULTIBLOCK_UPDATE_TICKS == 0) {
                multiblock.checkForNewNodes(this.pos);
            }

            if(ticksSinceCreation % SEND_RESOURCES_UPDATE_TICKS == 0) {
                sendResources();
            }
        }
    }
}
