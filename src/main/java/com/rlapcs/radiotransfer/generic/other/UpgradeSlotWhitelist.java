package com.rlapcs.radiotransfer.generic.other;

import com.rlapcs.radiotransfer.ModConstants;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;
import java.util.function.Predicate;

public class UpgradeSlotWhitelist {
    public static int findIndexWhereAllowed(ItemStack stack, Map<Integer, UpgradeSlotWhitelist> upgradeSlotWhitelists) {
        for(int s : upgradeSlotWhitelists.keySet()) {
            UpgradeSlotWhitelist wl = upgradeSlotWhitelists.get(s);
            if(wl.canInsertStack(stack)) return s;
        }
        return -1;
    }

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
    public UpgradeCardEntry getMatchingUpgradeCardEntry(ItemStack stack) {
        for(UpgradeCardEntry e : cardEntries) {
            if(e.canInsert(stack)) return e;
        }
        return null;
    }

    public void addEntry(UpgradeCardEntry entry) {
        cardEntries.add(entry);
    }

    @Override
    public String toString() {
        return "Whitelist: " + cardEntries.toString();
    }

    /* INNER CLASS */
    public static class UpgradeCardEntry {
        private Item item;

        private NBTTagCompound nbtTags; //only for tag names, not content
        private Predicate<ItemStack> otherRequirement;
        private int maxAmount;

        public UpgradeCardEntry(Item item, int maxAmount, NBTTagCompound requiredNbtTags, Predicate<ItemStack> otherRequirement) {
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
        public UpgradeCardEntry(Item item, int maxAmount, Predicate<ItemStack> otherRequirement) {
            this(item, maxAmount, null, otherRequirement);
        }
        public UpgradeCardEntry(int maxAmount, Predicate<ItemStack> otherRequirement) {
            this(null, maxAmount, otherRequirement);
        }
        public UpgradeCardEntry(Item item, int maxAmount) {
            this(item, maxAmount, (NBTTagCompound) null, null);
        }
        public UpgradeCardEntry(Item item) {
            this(item, ModConstants.UpgradeCards.DEFAULT_MAX_QUANTITY);
        }

        public boolean canInsert(ItemStack stack) {
            boolean itemsMatch = (!this.requiresItem() || this.item.equals(stack.getItem()));

            NBTTagCompound stackNBT = null;
            if(stack.hasTagCompound()) stackNBT = stack.getTagCompound();
            boolean nbtMatches = (!this.requiresNbtTags() || this.matchesNbtTags(stackNBT));

            boolean otherRequirementMatches = (!this.requiresOtherRequirement() || this.matchesOtherRequirement(stack));

            return itemsMatch && nbtMatches && otherRequirementMatches;
        }

        public boolean matchesNbtTags(NBTTagCompound stackTags) {
            if(stackTags == null) return false;

            for(String tag : nbtTags.getKeySet()) {
                if(!stackTags.hasKey(tag)) return false;
            }
            return true;
        }
        public boolean matchesOtherRequirement(ItemStack otherStack) {
            return otherRequirement.test(otherStack);
        }

        public boolean requiresNbtTags() {
            return nbtTags != null;
        }
        public boolean requiresOtherRequirement() { return otherRequirement != null; }
        public boolean requiresItem() { return item != null; }

        public Item getItem() {
            return item;
        }


        public NBTTagCompound getNbtTags() {
            return nbtTags;
        }

        public int getMaxAmount() {
            return maxAmount;
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

        @Override
        public String toString() {
            return item.toString();
        }
    }
}
