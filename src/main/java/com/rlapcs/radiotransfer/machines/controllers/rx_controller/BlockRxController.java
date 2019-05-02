package com.rlapcs.radiotransfer.machines.controllers.rx_controller;

import com.rlapcs.radiotransfer.RadioTransfer;
import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractBlockController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.TileTxController;
import net.minecraft.creativetab.CreativeTabs;

public class BlockRxController extends AbstractBlockController {
    public static final String NAME = "rx_controller";

    public BlockRxController() {
        super(TileRxController.class);

        setRegistryName(NAME);
        setUnlocalizedName(RadioTransfer.MODID + "." + NAME);
        setCreativeTab(CreativeTabs.MISC);
    }
}
