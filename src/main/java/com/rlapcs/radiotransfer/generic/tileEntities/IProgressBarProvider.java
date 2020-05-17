package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * An interface for tile entities which want to provide progress bars
 */
public interface IProgressBarProvider {
    void doProcess();
    boolean canDoProcessServer();
    default boolean canDoProcessClient() {return canDoProcessServer();}

    int getProcessTime();
    int getProcessTimeElapsed();
    void setProcessTimeElapsed(int target);

    default void doProcessUpdate(World world, int ticksSinceLastCall) {
        doServerProcessUpdate(world, ticksSinceLastCall);
        doClientProcessUpdate(world, ticksSinceLastCall);
    }

    /**
     * Instead of tracking the status of the server process with network packets, we're just going to assume that the
     * client and server ticks match, and update this one blind. Works well, and saves some network traffic.
     * @param world
     * @param ticksSinceLastCall
     */
    default void doClientProcessUpdate(World world, int ticksSinceLastCall) {
        if(world.isRemote) { //Client Side only
            if (getProcessTimeElapsed() >= getProcessTime()) {
                setProcessTimeElapsed(0);
            }
            else if (canDoProcessClient()) {
                incrementProcessTimeElapsed(ticksSinceLastCall);
                setProcessTimeElapsed(MathHelper.clamp(getProcessTimeElapsed(), 0, getProcessTime()));
            }
            else {
                setProcessTimeElapsed(0);
            }
        }
    }
    default void doServerProcessUpdate(World world, int ticksSinceLastCall) {
        if(!world.isRemote) { //Server Side only
            if (getProcessTimeElapsed() >= getProcessTime()) {
                doProcess();
                setProcessTimeElapsed(0);
            }
            else if (canDoProcessServer()) {
                incrementProcessTimeElapsed(ticksSinceLastCall);
                setProcessTimeElapsed(MathHelper.clamp(getProcessTimeElapsed(), 0, getProcessTime()));
            }
            else {
                setProcessTimeElapsed(0);
            }
        }
    }


    default void incrementProcessTimeElapsed(int by) {
        setProcessTimeElapsed(getProcessTimeElapsed() + by);
    }
    default double getFractionOfProcessCompleted() {
        return MathHelper.clamp((double) getProcessTimeElapsed() / (double) getProcessTime(), 0, 1.0);
    }
}
