package com.rlapcs.radiotransfer.proxy;

import com.rlapcs.radiotransfer.client.guis.GuiDemoBlock;
import com.rlapcs.radiotransfer.client.guis.GuiTransmitter;
import com.rlapcs.radiotransfer.common.containers.ContainerDemoBlock;
import com.rlapcs.radiotransfer.common.containers.ContainerTransmitter;
import com.rlapcs.radiotransfer.common.tileEntities.TileDemoBlock;
import com.rlapcs.radiotransfer.common.tileEntities.TileTransmitter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class GuiProxy implements IGuiHandler {
    /* List all Gui Entries here: */ //note: could also use static method and arraylist
    public static final GuiEntry<TileDemoBlock, GuiDemoBlock, ContainerDemoBlock> demoblock = new GuiEntry<>(TileDemoBlock.class, GuiDemoBlock.class, ContainerDemoBlock.class);
    public static final GuiEntry<TileTransmitter, GuiTransmitter, ContainerTransmitter> transmitter = new GuiEntry(TileTransmitter.class, GuiTransmitter.class, ContainerTransmitter.class);
    /* end gui entry list */

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);

        for(GuiEntry entry : getAllGuiEntries()) {
            if(entry.getGuiID() == ID) {
                return entry.getNewContainer(player.inventory, entry.castToTileEntityType(te));
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);

        for(GuiEntry entry : getAllGuiEntries()) {
            if(entry.getGuiID() == ID) {
                return entry.getNewGuiScreen(entry.castToTileEntityType(te), entry.getNewContainer(player.inventory, entry.castToTileEntityType(te)));
            }
        }
        return null;
    }

    public static int getIDFromTileEntiyClass(Class<? extends TileEntity> tileEntityClass) throws RuntimeException {
        for(GuiEntry entry : getAllGuiEntries()) {
            if(tileEntityClass.getName().equals(entry.getTileEntityClass().getName())) {
                return entry.getGuiID();
            }
        }

        throw new RuntimeException("no GUI_ID found for tileEntity class" + tileEntityClass.getName());
    }

    public static List<GuiEntry> getAllGuiEntries() {
        List<GuiEntry> guiEntries = new ArrayList<>();

        Field[] fields = GuiProxy.class.getDeclaredFields();
        for (Field f : fields) {
            if(Modifier.isStatic(f.getModifiers())) {
                Object fieldValue = null;
                try {
                    fieldValue = f.get(null);
                } catch (IllegalAccessException e) {
                    //Shouldn't happen
                    System.err.println("Error getting all GuiEntries from static fields.");
                    e.printStackTrace();
                }
                if (fieldValue instanceof GuiEntry) {
                    guiEntries.add((GuiEntry) fieldValue);
                }
            }
        }
        return guiEntries;
    }
}