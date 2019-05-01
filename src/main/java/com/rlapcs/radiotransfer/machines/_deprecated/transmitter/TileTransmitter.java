package com.rlapcs.radiotransfer.machines._deprecated.transmitter;

import com.rlapcs.radiotransfer.machines._deprecated.other.AbstractTileRadio;
import com.rlapcs.radiotransfer.server.radio.RadioNetwork;


public class TileTransmitter extends AbstractTileRadio {
    public static final int NUM_TICKS_BETWEEN_SEND = 20;
    public static final int MAX_STACK_SIZE_IN_PACKET = 16;

    public TileTransmitter() {
        super();
    }

    private void processSendItems() {
        if(this.activated) {
            for (int slot = 0; slot < itemStackHandler.getSlots(); slot++) {
                if (itemStackHandler.getStackInSlot(slot).isEmpty()) {
                    continue;
                } else {
                    //boolean itemsWereSent = RadioNetwork.INSTANCE.sendItems(this, slot, MAX_STACK_SIZE_IN_PACKET); //amount not yet implemented

                    //no need for block update
                    /*
                    if(itemsWereSent) {
                        //world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
                    }
                     */

                    return;
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if(!world.isRemote) {
            if (ticksSinceCreation % NUM_TICKS_BETWEEN_SEND == 0) {
                processSendItems();
            }
        }
    }
}