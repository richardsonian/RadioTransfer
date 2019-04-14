package com.rlapcs.radiotransfer.common.blocks;

import com.rlapcs.radiotransfer.*;
import com.rlapcs.radiotransfer.client.guis.GuiTransmitter;
import com.rlapcs.radiotransfer.common.network.PacketHandler;
import com.rlapcs.radiotransfer.common.network.PacketSendKey;
import com.rlapcs.radiotransfer.common.tileEntities.TileTransmitter;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTransmitter extends AbstractBlockWithGui {
    public BlockTransmitter() {
        super(Material.IRON, TileTransmitter.class, GuiTransmitter.class);
        setUnlocalizedName(RadioTransfer.MODID + ".transmitter");
        setRegistryName("transmitter");
    }

}