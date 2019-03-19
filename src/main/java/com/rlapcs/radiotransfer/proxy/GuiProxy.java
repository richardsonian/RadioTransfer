package com.rlapcs.radiotransfer.proxy;

import com.rlapcs.radiotransfer.client.guis.DemoBlockGui;
import com.rlapcs.radiotransfer.client.guis.TransmitterGui;
import com.rlapcs.radiotransfer.common.containers.DemoContainer;
import com.rlapcs.radiotransfer.common.containers.TransmitterContainer;
import com.rlapcs.radiotransfer.common.tileEntities.DemoBlockTileEntity;
import com.rlapcs.radiotransfer.common.tileEntities.TransmitterTileEntity;
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
        switch (ID) {
            case 1:
                return new DemoContainer(player.inventory, (DemoBlockTileEntity) te);
            case 2:
                return new TransmitterContainer(player.inventory, (TransmitterTileEntity) te);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case 1:
                return new DemoBlockGui((DemoBlockTileEntity) te, new DemoContainer(player.inventory,
                        (DemoBlockTileEntity) te));
            case 2:
                return new TransmitterGui((TransmitterTileEntity) te, new TransmitterContainer(player.inventory,
                        (TransmitterTileEntity) te));
            default:
                return null;
        }
    }
}