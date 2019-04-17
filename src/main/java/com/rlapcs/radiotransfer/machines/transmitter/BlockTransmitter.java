package com.rlapcs.radiotransfer.machines.transmitter;

import com.rlapcs.radiotransfer.*;
import com.rlapcs.radiotransfer.generic.blocks.AbstractBlockMachineWithGui;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockTransmitter extends AbstractBlockMachineWithGui {
    public BlockTransmitter() {
        super(Material.IRON, TileTransmitter.class);

        setUnlocalizedName(RadioTransfer.MODID + ".transmitter");
        setRegistryName("transmitter");
        setCreativeTab(CreativeTabs.MISC);
    }
}