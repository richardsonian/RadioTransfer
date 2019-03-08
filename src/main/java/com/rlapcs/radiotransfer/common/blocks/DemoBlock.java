package com.rlapcs.radiotransfer.common.blocks;

import com.rlapcs.radiotransfer.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DemoBlock extends Block {
    public DemoBlock() {
        super(Material.GRASS);
        setUnlocalizedName(RadioTransfer.MODID + ".demoblock");
        setRegistryName("demoblock");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
