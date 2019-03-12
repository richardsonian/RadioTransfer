package com.rlapcs.radiotransfer.common.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    @GameRegistry.ObjectHolder("radiotransfer:demoitem")
    public static DemoItem demoItem;

    @GameRegistry.ObjectHolder("radiotransfer:redgem")
    public static Item redGem;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        demoItem.initModel();
        ModelLoader.setCustomModelResourceLocation(redGem, 0, new ModelResourceLocation(redGem.getRegistryName(), "inventory"));
    }
}
