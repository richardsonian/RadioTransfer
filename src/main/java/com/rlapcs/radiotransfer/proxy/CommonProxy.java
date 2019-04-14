package com.rlapcs.radiotransfer.proxy;
import com.rlapcs.radiotransfer.common.blocks.ModBlocks;
import com.rlapcs.radiotransfer.common.items.ModItems;
import com.rlapcs.radiotransfer.common.network.PacketHandler;
import com.rlapcs.radiotransfer.common.tileEntities.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import static com.rlapcs.radiotransfer.RadioTransfer.instance;

@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        PacketHandler.registerMessages("radiotransfer");
    }

    public void init(FMLInitializationEvent e) {
        ModTileEntities.registerTileEntities(); //Put here so that @ObjectHolder fields have been filled (after registry events). Not sure if this will cause issues.
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //Register Blocks
        for(Block block : ModBlocks.getInstancesForRegistry()) {
            event.getRegistry().register(block);
        }

        //Register Tile Entities used to go here
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //Block Items
        for(Block block : ModBlocks.getAllBlocks()) {
            event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }

        //Normal Items
        for(Item item : ModItems.getInstancesForRegistry()) {
            event.getRegistry().register(item);
        }
    }
}