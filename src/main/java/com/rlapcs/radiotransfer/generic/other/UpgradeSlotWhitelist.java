package com.rlapcs.radiotransfer.generic.other;

import com.rlapcs.radiotransfer.ModConstants;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UpgradeSlotWhitelist {
    private Set<UpgradeCardEntry> cardEntries;

    public UpgradeSlotWhitelist() {
        cardEntries = new HashSet<>();
    }
    public UpgradeSlotWhitelist(UpgradeCardEntry... entries) {
        cardEntries = new HashSet<>(Arrays.asList(entries));
    }

    public boolean canInsertStack(ItemStack stack) {
        for(UpgradeCardEntry e : cardEntries) {
            if(e.canInsert(stack)) return true;
        }
        return false;
    }

    public void addEntry(UpgradeCardEntry entry) {
        cardEntries.add(entry);
    }

    public static class UpgradeCardEntry {
        private Item item;

        private NBTTagCompound nbtTags; //only for tag names, not content
        private int maxAmount;

        public UpgradeCardEntry(Item item, int maxAmount, NBTTagCompound requiredNbtTags) {
            this.item = item;
            this.maxAmount = maxAmount;
            this.nbtTags = requiredNbtTags;
        }
        public UpgradeCardEntry(Item item, int maxAmount, String... requiredNbtTags) {
            this(item, maxAmount);
            nbtTags = new NBTTagCompound();
            for(String s : requiredNbtTags) {
                nbtTags.setTag(s, null);
            }
        }
        public UpgradeCardEntry(Item item, int maxAmount) {
            this(item, maxAmount, (NBTTagCompound) null);
        }
        public UpgradeCardEntry(Item item) {
            this(item, ModConstants.UpgradeCards.DEFAULT_MAX_QUANTITY);
        }

        public boolean canInsert(ItemStack stack) {
            boolean itemsMatch = this.item.equals(stack.getItem());

            NBTTagCompound stackNBT = null;
            if(stack.hasTagCompound()) stackNBT = stack.getTagCompound();
            boolean nbtMatches = (!this.requiresNbtTags() || this.matchesNbtTags(stackNBT));

            return itemsMatch && nbtMatches;
        }

        public boolean matchesNbtTags(NBTTagCompound stackTags) {
            if(stackTags == null) return false;

            for(String tag : nbtTags.getKeySet()) {
                if(!stackTags.hasKey(tag)) return false;
            }
            return true;
        }

        public boolean requiresNbtTags() {
            return nbtTags != null;
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public NBTTagCompound getNbtTags() {
            return nbtTags;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.item, this.nbtTags, this.maxAmount);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof UpgradeCardEntry) {
                UpgradeCardEntry other = (UpgradeCardEntry) obj;
                return this.item.equals(other.item) && this.matchesNbtTags(other.nbtTags) && this.maxAmount == other.maxAmount;
            }
            else {
                return false;
            }
        }
    }
}
