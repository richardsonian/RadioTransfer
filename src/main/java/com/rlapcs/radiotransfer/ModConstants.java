package com.rlapcs.radiotransfer;

import com.rlapcs.radiotransfer.generic.other.UpgradeSlotWhitelist;
import com.rlapcs.radiotransfer.generic.other.UpgradeSlotWhitelist.UpgradeCardEntry;
import com.rlapcs.radiotransfer.registries.ModItems;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;

/**
 * This class is for global mod variables, but NOT CONFIG VARIABLES. Just meant to get some central information in one place,
 * but most of these shouldn't ever really change in the hands of the end user.
 */
public class ModConstants {
    public static class UpgradeCards {
        public static final int DEFAULT_MAX_QUANTITY = 64;

        //encryption
        public static final int ENCRYPTION_CARD_MAX_QUANTITY = 1;
        public static final UpgradeSlotWhitelist ENCRYPTION_CARD_WHITELIST = new UpgradeSlotWhitelist(
                new UpgradeCardEntry(ModItems.encryption_card, ENCRYPTION_CARD_MAX_QUANTITY, "linkedPlayer")
        );

        //speed
        public static final int SPEED_UPGRADE_MAX_QUANTITY = 16;
        public static final UpgradeSlotWhitelist SPEED_UPGRADE_WHITELIST = new UpgradeSlotWhitelist(
                new UpgradeCardEntry(ModItems.speed_upgrade, SPEED_UPGRADE_MAX_QUANTITY)
        );


        //stack
        public static final int STACK_UPGRADE_MAX_QUANTITY = 4;
        public static final int STACK_DOWNGRADE_MAX_QUANTITY = 1;
        public static final UpgradeSlotWhitelist STACK_UPGRADE_WHITELIST = new UpgradeSlotWhitelist(
                new UpgradeCardEntry(ModItems.stack_upgrade, STACK_UPGRADE_MAX_QUANTITY),
                new UpgradeCardEntry(ModItems.stack_downgrade, STACK_DOWNGRADE_MAX_QUANTITY)
        );

        //filter
        public static final int FILTER_CARD_MAX_QUANTITY = 1;
        public static final UpgradeSlotWhitelist FILTER_CARD_WHITELIST = new UpgradeSlotWhitelist(
                new UpgradeCardEntry(ModItems.filter_card, FILTER_CARD_MAX_QUANTITY)
        );


        //Power supply -> not really an upgrade per se, but can work in this framework
        public static final int POWER_ITEM_MAX_QUANTITY = 1;
        public static final UpgradeSlotWhitelist POWER_ITEM_WHITELIST = new UpgradeSlotWhitelist(
                new UpgradeCardEntry(POWER_ITEM_MAX_QUANTITY, itemStack ->
                   itemStack.hasCapability(CapabilityEnergy.ENERGY, null)
                )
        );
    }

    public static final ResourceLocation ICONS = new ResourceLocation(RadioTransfer.MODID, "textures/gui/icons.png");
}
