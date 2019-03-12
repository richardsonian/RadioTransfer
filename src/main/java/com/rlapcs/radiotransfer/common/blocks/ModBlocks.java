package com.rlapcs.radiotransfer.common.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
    @GameRegistry.ObjectHolder("radiotransfer:demoblock")
    public static DemoBlock demoBlock;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
    }
}