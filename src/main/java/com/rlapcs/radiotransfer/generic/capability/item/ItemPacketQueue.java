package com.rlapcs.radiotransfer.generic.capability.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemPacketQueue implements INBTSerializable<NBTTagCompound>, ITransferHandler {
    public static final int MAX_QUANTITY = 9999;
    public static final int MAX_BUFFERS = 15;

    private List<ItemBuffer> itemBuffers;

    public ItemPacketQueue() {
        itemBuffers = new ArrayList<>();
        (new ItemBuffer(ItemStack.EMPTY)).item = null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < itemBuffers.size(); i++)
        {
            ItemBuffer buff = itemBuffers.get(i);
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
        nbt.setInteger("Size", itemBuffers.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        //itemBuffers = new NonNullList<>();
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound buffTags = tagList.getCompoundTagAt(i);
            if(buffTags.hasKey("index") && buffTags.hasKey("quantity")) {
                int index = buffTags.getInteger("index");
                int quantity = buffTags.getInteger("quantity");
                if (index >= 0 && index < itemBuffers.size()) {
                    itemBuffers.set(index, new ItemBuffer(new ItemStack(buffTags), quantity));
                }
            }
        }
        onLoad();
    }

    @Override
    public boolean isEmpty() {
        for(ItemBuffer b : itemBuffers) {
            if (b != null && b.quantity > 0 && !b.item.isEmpty()) {
                return true;
            }
            else {
                itemBuffers.remove(b);
            }
        }
        return false;
    }
    public ItemStack getNextPacket(int maxAmount) {
        for(ItemBuffer b : itemBuffers) {
            if(b != null && b.quantity > 0 && !b.item.isEmpty()) {
                ItemStack stack = b.item.copy();
                int numToGive = MathHelper.clamp(b.quantity, 1, maxAmount);
                stack.setCount(numToGive);
                b.quantity -= numToGive;
                if(b.quantity == 0) itemBuffers.remove(b);
                onContentsChanged();
                return stack;
            }
            else {
                itemBuffers.remove(b);
                onContentsChanged();
            }
        }
        return ItemStack.EMPTY;
    }
    public ItemStack add(ItemStack stack) {
        if(stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack staq = stack.copy();
        for(ItemBuffer b : itemBuffers) {
            if(ItemHandlerHelper.canItemStacksStack(b.item, staq)) {
                int numToAdd = MathHelper.clamp(staq.getCount(), 0, MAX_QUANTITY - b.quantity);
                b.quantity += numToAdd;
                staq.shrink(numToAdd);
                this.onContentsChanged();
                return staq;
            }
        }
        if(itemBuffers.size() < MAX_BUFFERS) {
            itemBuffers.add(new ItemBuffer(stack)); //assuming MAX_QUANTITY is not below 64! Should actually check if can fit here
            this.onContentsChanged();
            return ItemStack.EMPTY;
        }

        return staq;
    }
    public void move(int fromIndex, int toIndex) {
        itemBuffers.add(toIndex, itemBuffers.remove(fromIndex));
        onContentsChanged();
    }
    protected void onContentsChanged() {} //should be overridden to mark tileEntity dirty
    protected void onLoad() {}

    private static class ItemBuffer {
        private ItemStack item;
        private int quantity;

        private ItemBuffer(ItemStack item, int quantity) {
            this.item = item.copy();
            this.item.setCount(1);
            this.quantity = quantity;
        }
        private ItemBuffer(ItemStack stack) {
            this(stack, stack.getCount());
        }
    }
}
