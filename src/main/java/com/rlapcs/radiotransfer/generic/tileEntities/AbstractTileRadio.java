package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.util.ITickable;

public abstract class AbstractTileRadio extends AbstractTileMachine implements ITickable {
    public static final int ITEM_STACK_HANDLER_SIZE = 12;

    protected boolean activated;
    protected int frequency;

    public AbstractTileRadio() {
        super(ITEM_STACK_HANDLER_SIZE);
        activated = true;
        frequency = 1;
    }

    public void setActivated(boolean target) {
        activated = target;
    }

    public void changeFrequency(int target) {
        frequency = target;
    }

    @Override
    public void update() {

    }
}
