package com.rlapcs.radiotransfer.items;

import com.rlapcs.radiotransfer.RadioTransfer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEncryptionCard extends Item implements ICustomItemModel{
    public ItemEncryptionCard() {
        setRegistryName("encryption_card");  // The unique name (within your mod) that identifies this item
        setUnlocalizedName(RadioTransfer.MODID + ".encryption_card");     // Used for localization (en_US.lang)
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
