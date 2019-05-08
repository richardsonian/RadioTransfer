package com.rlapcs.radiotransfer;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ModConstants {
    public static class UpgradeCards {
        //encryption
        public static final Set<Item> ENCRPYION_CARD_ITEMS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                Items.STICK, Items.LEAD
        )));
        public static final int ENCRYPTION_CARD_MAX_QUANTITY = 1;

        //speed
        public static final Set<Item> SPEED_UPGRADE_ITEMS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                Items.WHEAT, Items.CARROT
        )));
        public static final int SPEED_UPGRADE_MAX_QUANTITY = 16;

        //stack
        public static final Set<Item> STACK_UPGRADE_ITEMS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                Items.REDSTONE, Items.DIAMOND
        )));
        public static final int STACK_UPGRADE_MAX_QUANTITY = 4;
        public static final int STACK_DOWNGRADE_MAX_QUANTITY = 1;

        //filter
        public static final Set<Item> FILTER_CARD_ITEMS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                Items.BOOK
        )));
        public static final int FILTER_CARD_MAX_QUANTITY = 1;
    }

}
