package com.rlapcs.radiotransfer.machines.transmitter;

import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileRadio;
import com.rlapcs.radiotransfer.server.radio.RadioRegistry;
import net.minecraft.item.ItemStack;


public class TileTransmitter extends AbstractTileRadio {
    public static final int NUM_TICKS_BETWEEN_SEND = 20;
    public static final int MAX_STACK_SIZE_IN_PACKET = 16;

    private boolean registered;

    public TileTransmitter() {
        super();

        registered = false;
    }

    @Override
    public void onChunkUnload() {
        if(this.registered && !world.isRemote) {
            RadioRegistry.INSTANCE.deregister(this);
            registered = false;
        }
    }

    private void processSendItems() {
        if(this.activated) {
            for (int slot = 0; slot < itemStackHandler.getSlots(); slot++) {
                if (itemStackHandler.getStackInSlot(slot).isEmpty()) {
                    continue;
                } else {
                    RadioRegistry.INSTANCE.sendItems(this, slot, MAX_STACK_SIZE_IN_PACKET); //amount not yet implemented
                    world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
                    return;
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if(!world.isRemote) {
            if(ticksSinceCreation % 10 == 0 && !registered) {
                RadioRegistry.INSTANCE.register(this);
                registered = true;
            }

            if (ticksSinceCreation % NUM_TICKS_BETWEEN_SEND == 0) {
                processSendItems();
            }
        }
    }
}