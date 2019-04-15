package com.rlapcs.radiotransfer.proxy;

import com.rlapcs.radiotransfer.registries.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.client.event.ModelRegistryEvent;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModBlocks.initModels();
        ModItems.initModels();
    }
}