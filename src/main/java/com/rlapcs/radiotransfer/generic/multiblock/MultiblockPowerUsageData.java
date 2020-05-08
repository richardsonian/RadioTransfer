package com.rlapcs.radiotransfer.generic.multiblock;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.IGuiListContent;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class MultiblockPowerUsageData implements IGuiListContent, INBTSerializable<NBTTagCompound> {
    private Set<PowerUsageEntry> entries;

    public MultiblockPowerUsageData() {
        entries = new HashSet<>();
    }

    //For Client
    @Override
    public int size() {
        return entries.size();
    }

    //For Server
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (PowerUsageEntry entry : entries) {
            NBTTagCompound entryTag = entry.serializeNBT(); //make new tag
            nbtTagList.appendTag(entryTag); //append to list
        }

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("entries", nbtTagList);
        return nbt;
    }

    //For Client
    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        entries = new HashSet<>();
        if(nbt.hasKey("entries")) {
            NBTTagList nbtTagList = nbt.getTagList("entries", Constants.NBT.TAG_COMPOUND);
            for(int i=0; i<nbtTagList.tagCount(); i++) {
                entries.add(new PowerUsageEntry(nbtTagList.getCompoundTagAt(i)));
            }
        }
    }

    //For Server
    public void updateNode() {

    }
    public void updateAll() {
        
    }

/*~~~~~~~~~~~~~~~~~ENTRY SUBCLASS~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Contains all the power usage data for one multiblock node
     */
    public static class PowerUsageEntry implements INBTSerializable<NBTTagCompound> {
        public Class<? extends AbstractTileMultiblockNode> nodeClass;

        public Instant lastUpdated;

        public int basePowerPerTick;
        public Set<UpgradeCardPowerEntry> upgradeCardConstantCosts;
        public int effectivePowerPerTick;

        public int basePowerPerProcess;
        public Set<UpgradeCardPowerEntry> upgradeCardProcessCosts;
        public int effectivePowerPerProcess;
        public int processRate;
        public int averageProcessPowerPerTick;

        public int totalPowerPerTick;

        public PowerUsageEntry(){}
        public PowerUsageEntry(AbstractTileMultiblockNode te) {
            this.update(te);
            lastUpdated = Instant.now();
        }
        public PowerUsageEntry(NBTTagCompound nbt){
            this();
            deserializeNBT(nbt);
        }

        public void update(AbstractTileMultiblockNode te) {
            nodeClass = te.getClass();

            //Grab all the constant power values
            basePowerPerTick = te.getBasePowerPerTick();
            upgradeCardConstantCosts = new HashSet<>();
            te.getConstantPowerContributingUpgrades().forEach((u) -> upgradeCardConstantCosts.add(new UpgradeCardPowerEntry(u, te.getUpgradeCardConstantPowerCosts().get(u), te.getUpgradeCardQuantities().get(u))));
            effectivePowerPerTick = te.getPowerPerTick();

            //Grab all the process power values
            basePowerPerProcess = te.getBasePowerPerProcess();
            upgradeCardProcessCosts = new HashSet<>();
            te.getProcessPowerContributingUpgrades().forEach((u) -> upgradeCardConstantCosts.add(new UpgradeCardPowerEntry(u, te.getUpgradeCardProcessPowerCosts().get(u), te.getUpgradeCardQuantities().get(u))));
            effectivePowerPerProcess = te.getPowerPerProcess();
            processRate = te.getAverageProcessesRate();
            averageProcessPowerPerTick = te.getAverageProcessPowerPerTick(); //unnecessary

            //internal calculations
            totalPowerPerTick = effectivePowerPerTick + effectivePowerPerProcess;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setString("nodeClass", nodeClass.getName());
            //Constant Power Data
            nbt.setInteger("basePowerPerTick", basePowerPerTick);
            nbt.setInteger("effectivePowerPerTick", effectivePowerPerTick);
            //Process Power Data
            nbt.setInteger("basePowerPerProcess", basePowerPerProcess);
            nbt.setInteger("effectivePowerPerProcess", effectivePowerPerProcess);
            nbt.setInteger("processRate", processRate);
            nbt.setInteger("averageProcessPowerPerTick", averageProcessPowerPerTick);


            //Upgrade Card Constant Costs
            NBTTagList upgradeCardConstantCostsTag = new NBTTagList();
            for (UpgradeCardPowerEntry pe : upgradeCardConstantCosts) {
                upgradeCardConstantCostsTag.appendTag(pe.serializeNBT());
            }
            nbt.setTag("upgradeCardConstantCosts", upgradeCardConstantCostsTag);

            //Upgrade Card Process Costs
            NBTTagList upgradeCardProcessCostsTag = new NBTTagList();
            for (UpgradeCardPowerEntry pe : upgradeCardProcessCosts) {
                upgradeCardProcessCostsTag.appendTag(pe.serializeNBT());
            }
            nbt.setTag("upgradeCardProcessCosts", upgradeCardProcessCostsTag);

            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            if(nbt.hasKey("nodeClass")) {
                try {
                    nodeClass = (Class<? extends AbstractTileMultiblockNode>) Class.forName(nbt.getString("nodeClass"));
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            //Constant Power Data
            if(nbt.hasKey("basePowerPerTick")) {
                basePowerPerTick = nbt.getInteger("basePowerPerTick");
            }
            if(nbt.hasKey("effectivePowerPerTick")) {
                effectivePowerPerTick = nbt.getInteger("effectivePowerPerTick");
            }
            //Process Power Data
            if(nbt.hasKey("basePowerPerProcess")) {
                basePowerPerProcess = nbt.getInteger("basePowerPerProcess");
            }
            if(nbt.hasKey("effectivePowerPerProcess")) {
                effectivePowerPerProcess = nbt.getInteger("effectivePowerPerProcess");
            }
            if(nbt.hasKey("processRate")) {
                processRate = nbt.getInteger("processRate");
            }
            if(nbt.hasKey("averageProcessPowerPerTick")) {
                averageProcessPowerPerTick = nbt.getInteger("averageProcessPowerPerTick");
            }

            //Upgrade Card Constant Costs
            if(nbt.hasKey("upgradeCardConstantCosts")) {
                upgradeCardConstantCosts = new HashSet<>();
                NBTTagList upgradeCardConstantCostsTag = nbt.getTagList("upgradeCardConstantCosts", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < upgradeCardConstantCostsTag.tagCount(); i++) {
                    upgradeCardProcessCosts.add(new UpgradeCardPowerEntry(upgradeCardConstantCostsTag.getCompoundTagAt(i))); //inefficient?
                }
            }
            //Upgrade Card Process Costs
            if(nbt.hasKey("upgradeCardProcessCosts")) {
                upgradeCardProcessCosts = new HashSet<>();
                NBTTagList upgradeCardProcessCostsTag = nbt.getTagList("upgradeCardProcessCosts", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < upgradeCardProcessCostsTag.tagCount(); i++) {
                    upgradeCardProcessCosts.add(new UpgradeCardPowerEntry(upgradeCardProcessCostsTag.getCompoundTagAt(i))); //inefficient?
                }
            }

            //calculations
            totalPowerPerTick = effectivePowerPerTick + effectivePowerPerProcess;
        }

        /**
         * Contains the power usage info for a specific type of upgrade card
        */
        public static class UpgradeCardPowerEntry implements INBTSerializable<NBTTagCompound> {
            public Item item;
            public int quantity;
            public int cost;
            public int total;

            public UpgradeCardPowerEntry() {
                this(null, 0, 0);
            }
            public UpgradeCardPowerEntry(Item item, int quantity, int cost) {
                this.item = item;
                this.quantity = quantity;
                this.cost = cost;

                this.total = quantity * cost;
            }
            public UpgradeCardPowerEntry(NBTTagCompound nbt) {
                this();
                deserializeNBT(nbt);
            }

            @Override
            public NBTTagCompound serializeNBT() {
                NBTTagCompound nbt = new NBTTagCompound();

                //item
                ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(this.item);
                nbt.setString("item", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
                //ints
                nbt.setInteger("quantity", quantity);
                nbt.setInteger("cost", cost);

                return nbt;
            }

            @Override
            public void deserializeNBT(NBTTagCompound nbt) {
                this.item = nbt.hasKey("id", Constants.NBT.TAG_STRING) ? Item.getByNameOrId(nbt.getString("id")) : Items.AIR;
                if(nbt.hasKey("quantity")) {
                    this.quantity = nbt.getInteger("quantity");
                }
                if(nbt.hasKey("cost")) {
                    this.cost = nbt.getInteger("cost");
                }

                this.total = this.quantity * this.cost;
            }
        }
    }
}
