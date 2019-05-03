package com.rlapcs.radiotransfer.generic.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemPacketQueue implements INBTSerializable<NBTTagCompound>, ITransferHandler {
    public static final int MAX_QUANTITY = 999;
    public static final int MAX_BUFFERS = 15;

    private List<PacketBuffer> packetBuffers;

    public ItemPacketQueue() {
        packetBuffers = new ArrayList<>();
        (new PacketBuffer(ItemStack.EMPTY)).item = null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < packetBuffers.size(); i++)
        {
            PacketBuffer buff = packetBuffers.get(i);
            if (buff != null && buff.quantity != 0 && !buff.item.isEmpty()) {
                NBTTagCompound bufferTag = new NBTTagCompound();
                bufferTag.setInteger("index", i);
                buff.item.writeToNBT(bufferTag);
                bufferTag.setInteger("quantity", buff.quantity);
                nbtTagList.appendTag(bufferTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", packetBuffers.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        //packetBuffers = new NonNullList<>();
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound buffTags = tagList.getCompoundTagAt(i);
            if(buffTags.hasKey("index") && buffTags.hasKey("quantity")) {
                int index = buffTags.getInteger("index");
                int quantity = buffTags.getInteger("quantity");
                if (index >= 0 && index < packetBuffers.size()) {
                    packetBuffers.set(index, new PacketBuffer(new ItemStack(buffTags), quantity));
                }
            }
        }
        onLoad();
    }

    @Override
    public boolean isEmpty() {
        for(PacketBuffer b : packetBuffers) {
            if (b != null && b.quantity > 0 && !b.item.isEmpty()) {
                return true;
            }
            else {
                packetBuffers.remove(b);
            }
        }
        return false;
    }
    public ItemStack getNextPacket(int maxAmount) {
        for(PacketBuffer b : packetBuffers) {
            if(b != null && b.quantity > 0 && !b.item.isEmpty()) {
                ItemStack stack = b.item.copy();
                int numToGive = MathHelper.clamp(b.quantity, 1, maxAmount);
                stack.setCount(numToGive);
                b.quantity -= numToGive;
                if(b.quantity == 0) packetBuffers.remove(b);
                onContentsChanged();
                return stack;
            }
            else {
                packetBuffers.remove(b);
                onContentsChanged();
            }
        }
        return ItemStack.EMPTY;
    }
    public ItemStack add(ItemStack stack) {
        if(stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack staq = stack.copy();
        for(PacketBuffer b : packetBuffers) {
            if(ItemHandlerHelper.canItemStacksStack(b.item, staq)) {
                int numToAdd = MathHelper.clamp(staq.getCount(), 0, MAX_QUANTITY - b.quantity);
                b.quantity += numToAdd;
                staq.shrink(numToAdd);
                this.onContentsChanged();
                return staq;
            }
        }
        if(packetBuffers.size() < MAX_BUFFERS) {
            packetBuffers.add(new PacketBuffer(stack)); //assuming MAX_QUANTITY is not below 64! Should actually check if can fit here
            this.onContentsChanged();
            return ItemStack.EMPTY;
        }

        return staq;
    }
    public void move(int fromIndex, int toIndex) {
        packetBuffers.add(toIndex, packetBuffers.remove(fromIndex));
        onContentsChanged();
    }
    protected void onContentsChanged() {} //should be overridden to mark tileEntity dirty
    protected void onLoad() {}

    public static class PacketBuffer {
        private ItemStack item;
        private int quantity;

        private PacketBuffer(ItemStack item, int quantity) {
            this.item = item.copy();
            this.item.setCount(1);
            this.quantity = quantity;
        }
        private PacketBuffer(ItemStack stack) {
            this(stack, stack.getCount());
        }
    }
}
