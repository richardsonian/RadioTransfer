package com.rlapcs.radiotransfer;

import com.rlapcs.radiotransfer.proxy.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = RadioTransfer.MODID, name = RadioTransfer.MODNAME, version = RadioTransfer.MODVERSION,
        dependencies = "required-after:forge@[11.16.0.1865,)", useMetadata = true)
public class RadioTransfer {

    public static final String MODID = "radiotransfer";
    public static final String MODNAME = "Radio Transfer";
    public static final String MODVERSION = "0.0.1";

    @SidedProxy(clientSide = "com.rlapcs.radiotransfer.proxy.ClientProxy",
            serverSide = "com.rlapcs.radiotransfer.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static RadioTransfer instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

}