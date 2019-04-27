package com.rlapcs.radiotransfer.machines.controllers.rx_controller;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractBlockController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.TileTxController;
import net.minecraft.creativetab.CreativeTabs;

public class BlockRxController extends AbstractBlockController {
    public BlockRxController() {
        super(TileRxController.class);

        setRegistryName("rx_controller");
        setUnlocalizedName(RadioTransfer.MODID + "." + getRegistryName());
        setCreativeTab(CreativeTabs.MISC);
    }
}
