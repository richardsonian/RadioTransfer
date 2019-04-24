package com.rlapcs.radiotransfer.machines.receiver;

import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileRadio;
import com.rlapcs.radiotransfer.server.radio.RadioRegistry;
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
            RadioRegistry.INSTANCE.register(this);
            registered = true;
        }
    }

    public void deregister() {
        if(registered) {
            RadioRegistry.INSTANCE.deregister(this);
            registered = false;
        }
    }

    public void invalidate() {
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

    public void setPriority(int priority) {
        this.priority = MathHelper.clamp(priority, RadioRegistry.MIN_PRIORITY, RadioRegistry.MAX_PRIORITY);
        this.markDirty();
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
