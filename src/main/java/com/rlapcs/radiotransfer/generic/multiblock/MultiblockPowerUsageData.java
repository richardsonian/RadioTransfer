package com.rlapcs.radiotransfer.generic.multiblock;

import com.rlapcs.radiotransfer.ModConfig;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.ITooltipContent;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.IGuiListContent;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import com.rlapcs.radiotransfer.registries.ModBlocks;
import com.rlapcs.radiotransfer.util.Debug;
import com.rlapcs.radiotransfer.util.NBTUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is so incredibly memory and network inefficient but I don't care
 */
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

    public List<PowerUsageEntry> getSortedEntries() {
        List<PowerUsageEntry> outList = new ArrayList<>(entries);
        outList.sort(null); //sort by natural ordering (see PowerUsageEntry#compareTo())
        return outList;
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
    public void updateAll(List<AbstractTileMultiblockNode> nodes) {
        entries = new HashSet<>();
        entries.add(getRadioEntry());
        for(AbstractTileMultiblockNode n : nodes) {
            if(n != null) {
                entries.add(new PowerUsageEntry(n));
            }
        }
    }
    private PowerUsageEntry getRadioEntry() {
        PowerUsageEntry radio = new PowerUsageEntry();
        radio.block = ModBlocks.radio;
        radio.basePowerPerTick = ModConfig.power_options.radio.powerPerTick;
        radio.totalPowerPerTick = radio.basePowerPerTick;
        radio.upgradeCardConstantCosts = new HashSet<>();
        radio.upgradeCardProcessCosts = new HashSet<>();

        return radio;
    }
/*~~~~~~~~~~~~~~~~~ENTRY SUBCLASS~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Contains all the power usage data for one multiblock node
     */
    public static class PowerUsageEntry implements INBTSerializable<NBTTagCompound>, Comparable<PowerUsageEntry>, ITooltipContent {
        public Block block;

        public Instant lastUpdated;

        public int basePowerPerTick;
        public Set<UpgradeCardPowerEntry> upgradeCardConstantCosts;
        public int effectivePowerPerTick;

        public int basePowerPerProcess;
        public Set<UpgradeCardPowerEntry> upgradeCardProcessCosts;
        public int effectivePowerPerProcess;
        public double processRate;
        public double averageProcessPowerPerTick;

        public double totalPowerPerTick;

        public PowerUsageEntry(){
            lastUpdated = Instant.now();
        }
        public PowerUsageEntry(AbstractTileMultiblockNode te) {
            this.update(te);
        }
        public PowerUsageEntry(NBTTagCompound nbt){
            this();
            deserializeNBT(nbt);
        }

        public void update(AbstractTileMultiblockNode te) {
            //get the block from the TE
            block = te.getWorld().getBlockState(te.getPos()).getBlock();

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
            totalPowerPerTick = effectivePowerPerTick + averageProcessPowerPerTick;

            lastUpdated = Instant.now();
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setTag("lastUpdated", NBTUtils.serializeInstant(lastUpdated));

            Debug.sendDebugMessage("Serializing Block " + block.getRegistryName().toString());
            nbt.setString("block", block.getRegistryName().toString());
            //Constant Power Data
            nbt.setInteger("powerPerTick", basePowerPerTick);
            nbt.setInteger("effectivePowerPerTick", effectivePowerPerTick);
            //Process Power Data
            nbt.setInteger("basePowerPerProcess", basePowerPerProcess);
            nbt.setInteger("effectivePowerPerProcess", effectivePowerPerProcess);
            nbt.setDouble("processRate", processRate);
            nbt.setDouble("averageProcessPowerPerTick", averageProcessPowerPerTick);


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
            if(nbt.hasKey("lastUpdated")) {
                lastUpdated = NBTUtils.deserializeInstant(nbt.getCompoundTag("lastUpdated"));
            }

            if(nbt.hasKey("block")) {
                block = Block.getBlockFromName(nbt.getString("block"));
            }
            //Constant Power Data
            if(nbt.hasKey("powerPerTick")) {
                basePowerPerTick = nbt.getInteger("powerPerTick");
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
                processRate = nbt.getDouble("processRate");
            }
            if(nbt.hasKey("averageProcessPowerPerTick")) {
                averageProcessPowerPerTick = nbt.getDouble("averageProcessPowerPerTick");
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

        @Override
        public String getFormattedContent() {
            return "test\nof\nthis\nidea, yay!";
        }

        @Override
        public int compareTo(PowerUsageEntry o) {
            return Double.compare(this.totalPowerPerTick, o.totalPowerPerTick);
        }

        //~~~~~~~~~~~~ToString Methods~~~~~~~~~~~~~~~~~~~//
        public String getTitle() {
            return block.getLocalizedName();
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

            /**
             * Get String representation of this card
             * @return String of format "×3 = +18"
             */
            @Override
            public String toString() {
                return String.format("x%d = +%d", quantity, total);
            }
        }
    }
}
