package com.rlapcs.radiotransfer.proxy;

import com.rlapcs.radiotransfer.generic.clientonly.guis.GuiEntry;
import com.rlapcs.radiotransfer.registries.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;


public class GuiProxy implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);

        for(GuiEntry entry : ModGuis.getAllGuiEntries()) {
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

        for(GuiEntry entry : ModGuis.getAllGuiEntries()) {
            if(entry.getGuiID() == ID) {
                return entry.getNewGuiScreen(entry.castToTileEntityType(te), entry.getNewContainer(player.inventory, entry.castToTileEntityType(te)));
            }
        }
        return null;
    }
}