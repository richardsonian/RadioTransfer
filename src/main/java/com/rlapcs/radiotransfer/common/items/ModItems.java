package com.rlapcs.radiotransfer.common.items;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    @GameRegistry.ObjectHolder("radiotransfer:demoitem")
    public static DemoItem demoItem;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        demoItem.initModel();
    }
}
