package com.rlapcs.radiotransfer.common.items;

import com.rlapcs.radiotransfer.RadioTransfer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DemoItem extends Item {
    public DemoItem() {
        setRegistryName("demoitem");  // The unique name (within your mod) that identifies this item
        setUnlocalizedName(RadioTransfer.MODID + ".demoitem");     // Used for localization (en_US.lang)
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
