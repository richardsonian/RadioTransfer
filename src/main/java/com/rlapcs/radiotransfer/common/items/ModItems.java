package com.rlapcs.radiotransfer.common.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ObjectHolder("radiotransfer")
public class ModItems {

    @ObjectHolder("demoitem")
    public static final DemoItem demoItem = null;

    @ObjectHolder("redgem")
    public static final Item redGem = null;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        demoItem.initModel();
        //redgem
        ModelLoader.setCustomModelResourceLocation(redGem, 0, new ModelResourceLocation(redGem.getRegistryName(), "inventory"));
    }
}
