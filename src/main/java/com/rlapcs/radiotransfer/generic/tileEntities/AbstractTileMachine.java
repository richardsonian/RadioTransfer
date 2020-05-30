package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;

public abstract class AbstractTileMachine extends TileEntity implements ITickable {
    public static final double MINIMUM_DISTANCE_TO_USE = 64D;

    protected long ticksSinceCreation;

    public AbstractTileMachine() {
        super();

        ticksSinceCreation = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        return compound;
    }

    // When the world loads from disk, the server needs to send the TileEntity information to the client
    //  it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this:
    //  getUpdatePacket() and onDataPacket() are used for one-at-a-time TileEntity updates
    //  getUpdateTag() and handleUpdateTag() are used by vanilla to collate together into a single chunk update packet
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbtTagCompound = getUpdateTag();
        int metadata = getBlockMetadata();
        return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
        // can also use metadata here (dont)
    }

    /*
    Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
     */
    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);

        //don't need to send items because of container sync
        if(nbtTagCompound.hasKey(ITileItemHandlerProvider.NBT_TAG_NAME)) {
            nbtTagCompound.removeTag(ITileItemHandlerProvider.NBT_TAG_NAME);
        }

        return nbtTagCompound;
    }
    /*
    Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
     */
    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= MINIMUM_DISTANCE_TO_USE;
    }

    @Override
    public void update() {
        ticksSinceCreation++;
    }

    public long getTicksSinceCreation() {
        return ticksSinceCreation;
    }

    @Override
    public String toString() {
        return String.format("%s at [%d, %d, %d]", this.getClass().getSimpleName(), pos.getX(), pos.getY(), pos.getZ());
    }
}
