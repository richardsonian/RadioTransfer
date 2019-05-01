package com.rlapcs.radiotransfer.registries;

import com.rlapcs.radiotransfer.machines._deprecated.demo.ContainerDemoBlock;
import com.rlapcs.radiotransfer.machines._deprecated.demo.GuiDemoBlock;
import com.rlapcs.radiotransfer.machines._deprecated.demo.TileDemoBlock;
import com.rlapcs.radiotransfer.machines._deprecated.receiver.ContainerReceiver;
import com.rlapcs.radiotransfer.machines._deprecated.receiver.GuiReceiver;
import com.rlapcs.radiotransfer.machines._deprecated.receiver.TileReceiver;
import com.rlapcs.radiotransfer.machines._deprecated.transmitter.ContainerTransmitter;
import com.rlapcs.radiotransfer.machines._deprecated.transmitter.GuiTransmitter;
import com.rlapcs.radiotransfer.machines._deprecated.transmitter.TileTransmitter;
import com.rlapcs.radiotransfer.generic.guis.GuiHandler.GuiEntry;
import com.rlapcs.radiotransfer.machines.controllers.rx_controller.ContainerRxController;
import com.rlapcs.radiotransfer.machines.controllers.rx_controller.GuiRxController;
import com.rlapcs.radiotransfer.machines.controllers.rx_controller.TileRxController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.ContainerTxController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.GuiTxController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.TileTxController;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.ContainerItemEncoder;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.GuiItemEncoder;
import com.rlapcs.radiotransfer.machines.processors.item_processors.item_encoder.TileItemEncoder;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ModGuis {
    /* List all Gui Entries here: */
    public static final GuiEntry tx_controller = new GuiEntry(TileTxController.class, GuiTxController.class, ContainerTxController.class);
    public static final GuiEntry rx_controller = new GuiEntry(TileRxController.class, GuiRxController.class, ContainerRxController.class);
    public static final GuiEntry item_encoder = new GuiEntry(TileItemEncoder.class, GuiItemEncoder.class, ContainerItemEncoder.class);

    // DEPRECATED
    public static final GuiEntry demoblock = new GuiEntry(TileDemoBlock.class, GuiDemoBlock.class, ContainerDemoBlock.class);
    public static final GuiEntry transmitter = new GuiEntry(TileTransmitter.class, GuiTransmitter.class, ContainerTransmitter.class);
    public static final GuiEntry receiver = new GuiEntry(TileReceiver.class, GuiReceiver.class, ContainerReceiver.class);
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
