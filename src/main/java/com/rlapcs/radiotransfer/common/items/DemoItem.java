package com.rlapcs.radiotransfer.common.items;

import com.rlapcs.radiotransfer.RadioTransfer;
import net.minecraft.item.Item;

public class DemoItem extends Item {
    public DemoItem() {
        setRegistryName("demoitem");        // The unique name (within your mod) that identifies this item
        setUnlocalizedName(RadioTransfer.MODID + ".demoitem");     // Used for localization (en_US.lang)
    }

}
