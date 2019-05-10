package com.rlapcs.radiotransfer;

import com.rlapcs.radiotransfer.generic.other.UpgradeSlotWhitelist;
import com.rlapcs.radiotransfer.generic.other.UpgradeSlotWhitelist.UpgradeCardEntry;
import net.minecraft.init.Items;

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
                new UpgradeCardEntry(Items.IRON_INGOT, ENCRYPTION_CARD_MAX_QUANTITY, "linkedPlayer")
        );

        //speed
        public static final int SPEED_UPGRADE_MAX_QUANTITY = 16;
        public static final UpgradeSlotWhitelist SPEED_UPGRADE_WHITELIST = new UpgradeSlotWhitelist(
                new UpgradeCardEntry(Items.WHEAT, SPEED_UPGRADE_MAX_QUANTITY),
                new UpgradeCardEntry(Items.CARROT, SPEED_UPGRADE_MAX_QUANTITY)
        );


        //stack
        public static final int STACK_UPGRADE_MAX_QUANTITY = 4;
        public static final int STACK_DOWNGRADE_MAX_QUANTITY = 1;
        public static final UpgradeSlotWhitelist STACK_UPGRADE_WHITELIST = new UpgradeSlotWhitelist(
                new UpgradeCardEntry(Items.DIAMOND, STACK_UPGRADE_MAX_QUANTITY),
                new UpgradeCardEntry(Items.REDSTONE, STACK_DOWNGRADE_MAX_QUANTITY)
        );

        //filter
        public static final int FILTER_CARD_MAX_QUANTITY = 1;
        public static final UpgradeSlotWhitelist FILTER_CARD_WHITELIST = new UpgradeSlotWhitelist(
                new UpgradeCardEntry(Items.BOOK, FILTER_CARD_MAX_QUANTITY)
        );

    }

}
