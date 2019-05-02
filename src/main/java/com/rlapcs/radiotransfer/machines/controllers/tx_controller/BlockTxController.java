package com.rlapcs.radiotransfer.machines.controllers.tx_controller;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractBlockController;
import net.minecraft.creativetab.CreativeTabs;

public class BlockTxController extends AbstractBlockController {
    public static final String NAME = "tx_controller";
    public BlockTxController() {
        super(TileTxController.class);

        setRegistryName(NAME);
        setUnlocalizedName(RadioTransfer.MODID + "." + NAME);
        setCreativeTab(CreativeTabs.MISC);
    }


}
