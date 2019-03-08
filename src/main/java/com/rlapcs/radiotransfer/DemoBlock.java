package com.rlapcs.radiotransfer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class DemoBlock extends Block {
    public DemoBlock() {
        super(Material.GRASS);
        setUnlocalizedName(RadioTransfer.MODID + ".demoblock");
        setRegistryName("demoblock");
    }
}
