package com.rlapcs.radiotransfer.machines._deprecated.receiver;

import com.rlapcs.radiotransfer.machines._deprecated.other.AbstractTileRadio;
import com.rlapcs.radiotransfer.server.radio.RadioNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;

public class TileReceiver extends AbstractTileRadio implements ITickable {

    protected int priority;
    protected boolean registered;

    public TileReceiver() {
        priority = 1;
        registered = false;
    }

    public void register() {
        if(!registered){
            RadioNetwork.INSTANCE.register(this);
            registered = true;
        }
    }

    public void deregister() {
        if(registered) {
            RadioNetwork.INSTANCE.deregister(this);
            registered = false;
        }
    }

    public void invalidate() {
        super.invalidate();
        deregister();
    }

    @Override
    public void onChunkUnload() {
        if(this.registered && !world.isRemote) {
            this.deregister();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("priority")) {
            this.priority = compound.getInteger("activated");
        }
        this.registered = false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("priority", this.priority);

        return compound;
    }

    public int getPriority() {
        return priority;
    }

    private void setPriority(int priority) {
        this.priority = priority;
        this.markDirty();
    }

    public void changePriority(boolean toIncrement) {
        int newPriority = getPriority() + (toIncrement ? 1 : -1);
        setPriority(MathHelper.clamp(newPriority, RadioNetwork.MIN_PRIORITY, RadioNetwork.MAX_PRIORITY));
    }

    @Override
    public void update() {
        super.update();
        if(!world.isRemote) {
            if(ticksSinceCreation % 10 == 0 && !registered) {
                this.register();
            }
        }
    }
}