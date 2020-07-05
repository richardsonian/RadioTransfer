package com.rlapcs.radiotransfer.proxy;

import com.rlapcs.radiotransfer.machines.radio_cable.BakedModelLoader;
import com.rlapcs.radiotransfer.registries.*;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.client.event.ModelRegistryEvent;

import static com.rlapcs.radiotransfer.RadioTransfer.MODID;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        ModelLoaderRegistry.registerLoader(new BakedModelLoader());
        OBJLoader.INSTANCE.addDomain(MODID);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModBlocks.initModels();
        ModItems.initModels();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        ModBlocks.initItemModels();
    }
}