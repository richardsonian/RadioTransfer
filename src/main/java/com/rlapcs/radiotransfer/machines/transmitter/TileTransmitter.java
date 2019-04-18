package com.rlapcs.radiotransfer.machines.transmitter;

import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import net.minecraft.util.ITickable;


public class TileTransmitter extends AbstractTileMachine implements ITickable {
    public static final int ITEM_STACK_HANDLER_SIZE = 12;

    public TileTransmitter() {
        super(ITEM_STACK_HANDLER_SIZE);
    }

    @Override
    public void update() {

    }
}