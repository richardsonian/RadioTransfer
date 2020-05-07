package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.server.radio.RadioNetwork;
import com.rlapcs.radiotransfer.server.radio.TransferType;

public class TileRadio extends AbstractTileMachine { //power requirements?
    public final int MULTIBLOCK_UPDATE_TICKS = 20;
    public final int REGISTER_UPDATE_TICKS = 20;
    private final int SEND_RESOURCES_UPDATE_TICKS = 20;
    private static final int POWER_CHECK_TICKS = 40;


    private MultiblockRadioController multiblock;

    public TileRadio() {
        super();
        multiblock = new MultiblockRadioController(this);
    }

    private void sendResources() {
        if(multiblock.canTransmit(TransferType.ITEM)) {
            RadioNetwork.INSTANCE.sendItems(multiblock, 16, multiblock.getTransmitMode(TransferType.ITEM));
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if(!world.isRemote) {
            multiblock.deregisterFromNetwork();
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
            multiblock.deregisterFromNetwork();
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
            if(ticksSinceCreation % REGISTER_UPDATE_TICKS == 0 && !multiblock.isRegisteredToNetwork()) {
                multiblock.registerToNetwork();
            }
            if(ticksSinceCreation % SEND_RESOURCES_UPDATE_TICKS == 0) {
                sendResources();
            }
            if(ticksSinceCreation % POWER_CHECK_TICKS == 0) { //separate into update use more frequently and update visual less frequently
                //use power from nodes
                if(multiblock.hasSufficientPower(POWER_CHECK_TICKS)) {
                    if(!multiblock.isPowered()) multiblock.setPowered(true);
                    multiblock.usePower(POWER_CHECK_TICKS);
                }
                else {
                    multiblock.setPowered(false);
                    //Debug.sendToAllPlayers(TextFormatting.DARK_RED + this.toString() + " unpowered.", world);
                }
            }
        }
    }

    public MultiblockRadioController getMultiblockController() {
        return multiblock;
    }


}
