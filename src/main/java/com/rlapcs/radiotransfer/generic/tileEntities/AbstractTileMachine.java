package com.rlapcs.radiotransfer.generic.tileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public abstract class AbstractTileMachine extends TileEntity implements ITickable {
    public static final double MINIMUM_DISTANCE_TO_USE = 64D;

    protected ItemStackHandler itemStackHandler;
    protected long ticksSinceCreation;

    public AbstractTileMachine(int itemStackHandlerSize) {
        super();

        ticksSinceCreation = 0;

        itemStackHandler = new ItemStackHandler(itemStackHandlerSize) {
            @Override
            protected void onContentsChanged(int slot) {
                // We need to tell the tile entity that something has changed so
                // that the chest contents is persisted
                AbstractTileMachine.this.markDirty();
            }
        };
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", itemStackHandler.serializeNBT());
        return compound;
    }

    // When the world loads from disk, the server needs to send the TileEntity information to the client
    //  it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this:
    //  getUpdatePacket() and onDataPacket() are used for one-at-a-time TileEntity updates
    //  getUpdateTag() and handleUpdateTag() are used by vanilla to collate together into a single chunk update packet
    //  Not really required for this example since we only use the timer on the client, but included anyway for illustration
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound); //may be unnecessary to send over items

        int metadata = getBlockMetadata();
        return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        //do something with block metadata?
    }

    /*
    Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
     */
    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
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
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        }
        return super.getCapability(capability, facing);
    }

    /**
     * Attempts to merge a stack into an ItemHandler, and returns what was not able to be merged.
     * Returns ItemStack.EMPTY if the merge was 100% successful
     * @param stack
     * @return The remainder of the stack
     */
    public ItemStack mergeStackIntoInventory(ItemStack stack) {
        if(!world.isRemote) {
            if(stack.isEmpty() || stack == null) return ItemStack.EMPTY;

            ItemStack itemstack = stack.copy();
            int slot = 0;
            while(!itemstack.isEmpty() && slot < this.itemStackHandler.getSlots()) {
                itemstack = this.itemStackHandler.insertItem(slot, itemstack, false);
                slot++;
            }

            //No need for block update
            /*
            if(ItemStack.areItemStacksEqual(stack, itemstack)) { //if something was merged into the inventory
                //world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
            }
            */

            return itemstack;
        }

        return null;
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            ticksSinceCreation++;
        }
    }

    @Override
    public String toString() {
        return String.format("%s at [%d, %d, %d] @%d", this.getClass().getSimpleName(), pos.getX(), pos.getY(), pos.getZ(), this.hashCode());
    }
}
