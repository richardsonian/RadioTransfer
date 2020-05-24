package com.rlapcs.radiotransfer.machines.radio;

import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachineWithInventory;
import com.rlapcs.radiotransfer.server.radio.RadioNetwork;
import com.rlapcs.radiotransfer.server.radio.TransferType;

public class TileRadio extends AbstractTileMachineWithInventory { //power requirements?
    public final int MULTIBLOCK_UPDATE_TICKS = 20;
    public final int REGISTER_UPDATE_TICKS = 20;
    private final int SEND_RESOURCES_UPDATE_TICKS = 20;
    private static final int POWER_CHECK_TICKS = 1;


    private MultiblockRadioController multiblock;

    public TileRadio() {
        super(0);
        multiblock = new MultiblockRadioController(this);
    }

    private void sendResources() {
        if(multiblock.canTransmit(TransferType.ITEM)) {
            boolean success = RadioNetwork.INSTANCE.sendItems(multiblock, 16, multiblock.getTransmitMode(TransferType.ITEM));
            if(success) {
                multiblock.getTxController().doProcess();
            }
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
            if(ticksSinceCreation % POWER_CHECK_TICKS == 0) {
                //Debug.sendToAllPlayers(String.format("%s[CONST PWR]%s %dFE/t x %dt = %dFE", TextFormatting.YELLOW, TextFormatting.GRAY, multiblock.calculateRequiredPowerPerTick(), POWER_CHECK_TICKS, (multiblock.calculateRequiredPowerPerTick() * POWER_CHECK_TICKS)), world);
                //Debug.sendToAllPlayers(multiblock.hasSufficientConstantPower(POWER_CHECK_TICKS) ? "Has enough power" : "Not enough power",world);
                //use power from nodes
                if(multiblock.hasSufficientConstantPower(POWER_CHECK_TICKS)) {

                    if(!multiblock.isPowered()) multiblock.setPowered(true);
                    multiblock.useConstantPower(POWER_CHECK_TICKS); //assuming we don't have to check whether this was successful, because we already checked that there was enough power. If something went wrong, we'll catch it in the next cycle.
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
