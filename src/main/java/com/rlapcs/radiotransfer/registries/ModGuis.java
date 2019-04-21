package com.rlapcs.radiotransfer.registries;

import com.rlapcs.radiotransfer.machines.demo.ContainerDemoBlock;
import com.rlapcs.radiotransfer.machines.demo.GuiDemoBlock;
import com.rlapcs.radiotransfer.machines.demo.TileDemoBlock;
import com.rlapcs.radiotransfer.machines.transmitter.ContainerTransmitter;
import com.rlapcs.radiotransfer.machines.transmitter.GuiTransmitter;
import com.rlapcs.radiotransfer.machines.transmitter.TileTransmitter;
import com.rlapcs.radiotransfer.generic.guis.GuiHandler.GuiEntry;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ModGuis {
    /* List all Gui Entries here: */
    public static final GuiEntry demoblock = new GuiEntry(TileDemoBlock.class, GuiDemoBlock.class, ContainerDemoBlock.class);
    public static final GuiEntry transmitter = new GuiEntry(TileTransmitter.class, GuiTransmitter.class, ContainerTransmitter.class);
    /* end gui entry list */



    public static int getGuiIDFromTileEntityClass(Class<? extends TileEntity> tileEntityClass) throws RuntimeException {
        for(GuiEntry entry : getAllGuiEntries()) {
            if(tileEntityClass.getName().equals(entry.getTileEntityClass().getName())) {
                return entry.getGuiID();
            }
        }

        throw new RuntimeException("no GUI_ID found for tileEntity class" + tileEntityClass.getName());
    }

    public static List<GuiEntry> getAllGuiEntries() {
        List<GuiEntry> guiEntries = new ArrayList<>();

        Field[] fields = ModGuis.class.getDeclaredFields();
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
