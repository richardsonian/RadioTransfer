package com.rlapcs.radiotransfer.proxy;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

//note: are Generics types and instance variable class objects redundant ( can we get rid of one of them?)

public class GuiEntry<T extends TileEntity, G extends GuiScreen, C extends Container> {
    private static int NEXT_GUI_ID;

    private int GUI_ID;
    private Class<T> tileEntityClass;
    private Class<G> guiClass;
    private Class<C> containerClass;

    public GuiEntry(Class<T> tileEntityClass, Class<G> guiClass, Class<C> containerClass) {

        NEXT_GUI_ID++;
        this.GUI_ID = NEXT_GUI_ID;

        this.tileEntityClass = tileEntityClass;
        this.guiClass = guiClass;
        this.containerClass = containerClass;
    }

    public int getGuiID() {
        return GUI_ID;
    }

    public Class<T> getTileEntityClass() {
        return this.tileEntityClass;
    }

    public T castToTileEntityType(TileEntity te) {
        try {
            return (T) te;
        } catch(ClassCastException e) {
            e.printStackTrace();
            System.err.println("Class casting error when casting tile entity " + te + " to type " + tileEntityClass.getName() + " with GuiEntry " + this);
            return null;
        }
    }

    public C getNewContainer(IInventory inventory, T te) {
        try {
            return (C) containerClass.getConstructor(IInventory.class, tileEntityClass).newInstance(inventory, te);
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

    public G getNewGuiScreen(T te, C container) {
        try {
            return (G) guiClass.getConstructor(tileEntityClass, containerClass).newInstance(te, container);
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
