package com.rlapcs.radiotransfer.generic.guis;

import com.rlapcs.radiotransfer.registries.*;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;


public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);

        for(GuiEntry entry : ModGuis.getAllGuiEntries()) {
            if(entry.getGuiID() == ID) {
                return entry.getNewContainer(player.inventory, entry.castToTileEntityType(te)); //is null if there is no container
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);

        for(GuiEntry entry : ModGuis.getAllGuiEntries()) {
            if(entry.getGuiID() == ID) {
                return entry.getNewGuiScreen(entry.castToTileEntityType(te), entry.getNewContainer(player.inventory, entry.castToTileEntityType(te)));
            }
        }
        return null;
    }

    public static class GuiEntry {
        private static int NEXT_GUI_ID;

        private int GUI_ID;
        private Class<? extends TileEntity> tileEntityClass;
        private Class<? extends GuiScreen> guiClass;
        private Class<? extends Container> containerClass;

        public GuiEntry( Class<? extends TileEntity> tileEntityClass, Class<? extends GuiScreen> guiClass, Class<? extends Container> containerClass) {

            NEXT_GUI_ID++;
            this.GUI_ID = NEXT_GUI_ID;

            this.tileEntityClass = tileEntityClass;
            this.guiClass = guiClass;
            this.containerClass = containerClass;
        }

        public int getGuiID() {
            return GUI_ID;
        }

        public Class<? extends TileEntity> getTileEntityClass() {
            return this.tileEntityClass;
        }

        //is this necessary?
        public TileEntity castToTileEntityType(TileEntity te) {
            try {
                return tileEntityClass.cast(te);
            } catch(ClassCastException e) {
                e.printStackTrace();
                System.err.println("Class casting error when casting tile entity " + te + " to type " + tileEntityClass.getName() + " with GuiEntry " + this);
                return null;
            }
        }

        public Container getNewContainer(IInventory inventory, TileEntity te) {
            if(!tileEntityClass.isInstance(te)) {
                throw new RuntimeException("Tile entity provided to container in Gui Handler is not of type " + tileEntityClass.getName());
            }
            if(containerClass==null) return null; //Tag on fix to return null to server if there's no container class
            try {
                return containerClass.getConstructor(IInventory.class, tileEntityClass).newInstance(inventory, te);
            } catch(NoSuchMethodException e) {
                //redundant with ReflectiveOperationException
                e.printStackTrace();
                System.err.println("Container class in GuiEntry " + this + "does not have a valid constructor");
                return null;
            } catch(ReflectiveOperationException e) {
                e.printStackTrace();
                System.err.println("Other reflective exception found");
                return null;
            } catch(ClassCastException e) {
                //this should never happen
                e.printStackTrace();
                System.err.println("Class cast exception casting to Container class " + containerClass.getName() + " in GuiEntry " + this);
                return null;
            }
        }

        public GuiScreen getNewGuiScreen(TileEntity te, Container container) {
            if(!tileEntityClass.isInstance(te)) {
                throw new RuntimeException("Tile entity provided to container in Gui Handler in " + this + " is not of type " + tileEntityClass.getName());
            }
            if(!containerClass.isInstance(container)) {
                throw new RuntimeException("Container provided to container in Gui Handler in " + this + " is not of type " + containerClass.getName());
            }
            try {
                return guiClass.getConstructor(tileEntityClass, containerClass).newInstance(te, container);
            } catch(NoSuchMethodException e) {
                //redundant with ReflectiveOperationException
                e.printStackTrace();
                System.err.println("Gui class in GuiEntry " + this + "does not have a valid constructor");
                return null;
            } catch(ReflectiveOperationException e) {
                e.printStackTrace();
                System.err.println("Other reflective exception found");
                return null;
            } catch(ClassCastException e) {
                //this should never happen
                e.printStackTrace();
                System.err.println("Class cast exception casting to Gui class " + guiClass.getName() + " in GuiEntry " + this);
                return null;
            }
        }

        @Override
        public String toString() {
            return String.format("GuiEntry<%s,%s,%s>_ID:%d_@%d", tileEntityClass.getName(), guiClass.getName(), containerClass.getName(), GUI_ID, hashCode());
        }

    }
}