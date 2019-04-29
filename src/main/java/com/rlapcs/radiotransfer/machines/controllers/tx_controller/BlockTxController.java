package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractBlockController;
import net.minecraft.creativetab.CreativeTabs;

public class BlockTxController extends AbstractBlockController {
    public BlockTxController() {
        super(TileTxController.class);

        setRegistryName("tx_controller");
        setUnlocalizedName(RadioTransfer.MODID + "." + getRegistryName());
        setCreativeTab(CreativeTabs.MISC);
    }


}
