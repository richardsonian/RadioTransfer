package com.rlapcs.radiotransfer.generic.multiblock;

import com.rlapcs.radiotransfer.ModConfig;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip.ITooltipContent;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists.IGuiListContent;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import com.rlapcs.radiotransfer.registries.ModBlocks;
import com.rlapcs.radiotransfer.util.Debug;
import com.rlapcs.radiotransfer.util.NBTUtils;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.time.Instant;
import java.util.*;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

/**
 * This class is so incredibly memory and network inefficient but I don't care
 */
public class MultiblockPowerUsageData implements IGuiListContent, INBTSerializable<NBTTagCompound> {
    public static final List<Block> NODE_ORDER;
    static {
        NODE_ORDER = new ArrayList<>();
        NODE_ORDER.add(ModBlocks.radio);
        NODE_ORDER.add(ModBlocks.tx_controller);
        NODE_ORDER.add(ModBlocks.rx_controller);
        NODE_ORDER.add(ModBlocks.item_encoder);
        NODE_ORDER.add(ModBlocks.item_decoder);
        NODE_ORDER.add(ModBlocks.power_supply);
        NODE_ORDER.add(ModBlocks.basic_antenna);
    }
    public static final Comparator<PowerUsageEntry> ENTRY_ORDERING = Comparator.comparingInt(n -> NODE_ORDER.indexOf(n.block));

    //Instance Vars
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
        outList.sort(ENTRY_ORDERING);
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
            if(n != null && !n.isInvalid()) { //If node is valid
                if(n.getPowerPerTick() != 0 || n.getPowerPerProcess() != 0) { //if node requires power
                    entries.add(new PowerUsageEntry(n));
                }
            }
        }
    }
    private PowerUsageEntry getRadioEntry() {
        PowerUsageEntry radio = new PowerUsageEntry();
        radio.block = ModBlocks.radio;
        radio.isActive = true;
        radio.basePowerPerTick = ModConfig.power_options.radio.powerPerTick;
        radio.effectivePowerPerTick = radio.basePowerPerTick;
        radio.totalPowerPerTick = radio.basePowerPerTick;

        radio.basePowerPerProcess = 0;
        radio.effectivePowerPerProcess = 0;
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

        public boolean isActive;
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
            //Get the Active State
            isActive = te.isActive();

            //Grab all the constant power values
            basePowerPerTick = te.getBasePowerPerTick();
            upgradeCardConstantCosts = new HashSet<>();
            te.getConstantPowerContributingUpgrades().forEach((u) -> upgradeCardConstantCosts.add(new UpgradeCardPowerEntry(u, te.getUpgradeCardConstantPowerCosts().get(u), te.getUpgradeCardQuantities().get(u))));
            effectivePowerPerTick = te.getPowerPerTick();

            //Grab all the process power values
            basePowerPerProcess = te.getBasePowerPerProcess();
            upgradeCardProcessCosts = new HashSet<>();
            te.getProcessPowerContributingUpgrades().forEach((u) -> upgradeCardProcessCosts.add(new UpgradeCardPowerEntry(u, te.getUpgradeCardProcessPowerCosts().get(u), te.getUpgradeCardQuantities().get(u))));
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

            //Debug.sendDebugMessage("Serializing Block " + block.getRegistryName().toString());
            nbt.setString("block", block.getRegistryName().toString());
            nbt.setBoolean("isActive", isActive);
            //Constant Power Data
            nbt.setInteger("basePowerPerTick", basePowerPerTick);
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
            if(nbt.hasKey("isActive")) {
                isActive = nbt.getBoolean("isActive");
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
                    upgradeCardConstantCosts.add(new UpgradeCardPowerEntry(upgradeCardConstantCostsTag.getCompoundTagAt(i))); //inefficient?
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
            totalPowerPerTick = effectivePowerPerTick + averageProcessPowerPerTick;
        }
        public List<UpgradeCardPowerEntry> getSortedUpgradeCardConstantCosts() {
            ArrayList<UpgradeCardPowerEntry> out = new ArrayList<>(upgradeCardConstantCosts);
            out.removeIf(u -> u.quantity == 0);
            out.sort(Comparator.comparingInt(u -> u.total));
            return out;
        }
        public List<UpgradeCardPowerEntry> getSortedUpgradeCardProcessCosts() {
            ArrayList<UpgradeCardPowerEntry> out = new ArrayList<>(upgradeCardProcessCosts);
            out.removeIf(u -> u.total == 0);
            out.sort(Comparator.comparingInt(u -> u.total));
            return out;
        }


        public boolean requiresConstantPower() {
            if(effectivePowerPerTick > 0) return true;
            //Account for when inactive
            boolean hasUpgradeCost = false;
            for(UpgradeCardPowerEntry u : upgradeCardConstantCosts) {
                if(u.total > 0) hasUpgradeCost = true;
            }
            return ((basePowerPerTick > 0) || hasUpgradeCost);
        }
        public boolean requiresProcessPower() {
            if(effectivePowerPerProcess > 0) return true;
            //Account for when inactive
            boolean hasUpgradeCost = false;
            for(UpgradeCardPowerEntry u : upgradeCardProcessCosts) {
                if(u.total > 0) hasUpgradeCost = true;
            }
            return ((basePowerPerProcess > 0) || hasUpgradeCost);
        }


        @Override
        public String getFormattedContent() {
            return this.toString();
        }

        /**
         * Indicates the natural ordering of a list of PowerUsageEntries.
         * One PowerUsageEntry is greater than other if its totalPowerPerTick is greater
         * @param o the object to compare to
         * @return Positive if this is greater than o, negative otherwise, or 0 if equal
         */
        @Override
        public int compareTo(PowerUsageEntry o) {
            return Double.compare(this.totalPowerPerTick, o.totalPowerPerTick);
        }

        //~~~~~~~~~~~~ToString Methods~~~~~~~~~~~~~~~~~~~//
        public String getTitle() {
            return block.getLocalizedName();
        }

        @Override
        public String toString() { //For debug
            String str = TextFormatting.DARK_PURPLE + "PowerUsageEntry: " + block.getLocalizedName() + TextFormatting.RESET;
            str += "\n - basePowerPerTick: " + basePowerPerTick;
            for(UpgradeCardPowerEntry u : getSortedUpgradeCardConstantCosts()) {
                str += "\n   - " + u.getDebugString();
            }
            str += "\n - effectivePowerPerTick: " + effectivePowerPerTick;
            str += "\n - basePowerPerProcess: " + basePowerPerProcess;
            for(UpgradeCardPowerEntry u : getSortedUpgradeCardProcessCosts()) {
                str += "\n   - " + u.getDebugString();
            }
            str += "\n - effectivePowerPerProcess: " + effectivePowerPerProcess;
            str += "\n - processRate: " + processRate;
            str += "\n - averageProcessPowerPerTick: " + averageProcessPowerPerTick;
            str += "\n - totalPowerPerTick: " + totalPowerPerTick;


            return str;
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
            public UpgradeCardPowerEntry(Item item, int cost, int quantity) {
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
                //sendDebugMessage("Serializing Item " + item);
                ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(this.item);
                nbt.setString("item", resourcelocation == null ? "minecraft:fish" : resourcelocation.toString());
                //ints
                nbt.setInteger("quantity", quantity);
                nbt.setInteger("cost", cost);

                return nbt;
            }

            @Override
            public void deserializeNBT(NBTTagCompound nbt) {
                if(nbt.hasKey("item", Constants.NBT.TAG_STRING)) {
                    this.item = Item.getByNameOrId(nbt.getString("item"));
                } else {this.item = Items.FISH;}
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
                return String.format("×%d = +%d", quantity, total);
            }
            public String getDebugString() { //CLIENT ONLY OR WILL CRASH
                return String.format("%s (%dFE)x%d = +%dFE", I18n.format(item.getUnlocalizedName()), cost, quantity, total);
            }
        }
    }
}
