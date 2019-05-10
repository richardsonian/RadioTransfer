package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * An interface for tile entities which want to provide progress bars
 */
public interface IProgressBarProvider {
    void doProcess();
    boolean canDoProcess();

    int getProcessTime();
    int getProcessTimeElapsed();
    void setProcessTimeElapsed(int target);

    default void doProcessUpdate(World world, int ticksSinceLastCall) {
        if (getProcessTimeElapsed() >= getProcessTime()) {
            if(!world.isRemote) { //server side
                doProcess();
            }
            setProcessTimeElapsed(0);
        }
        else if (canDoProcess()) {
            incrementProcessTimeElapsed(ticksSinceLastCall);
            setProcessTimeElapsed(MathHelper.clamp(getProcessTimeElapsed(), 0, getProcessTime()));
        }
        else {
            setProcessTimeElapsed(0);
        }
    }

    default void incrementProcessTimeElapsed(int by) {
        setProcessTimeElapsed(getProcessTimeElapsed() + by);
    }
    default double getFractionOfProcessCompleted() {
        return MathHelper.clamp((double) getProcessTimeElapsed() / (double) getProcessTime(), 0, 1.0);
    }
}
