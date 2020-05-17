package com.rlapcs.radiotransfer.network.messages.toClient;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import com.rlapcs.radiotransfer.generic.tileEntities.ITilePowerBarProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import scala.tools.nsc.transform.SpecializeTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageUpdateClientTileMultiblockNodePowered implements IMessage {
    private List<BlockPos> tilePoss;
    private boolean target;

    public MessageUpdateClientTileMultiblockNodePowered() {}
    public MessageUpdateClientTileMultiblockNodePowered(List<AbstractTileMultiblockNode> tes, boolean target) {
        this.tilePoss = new ArrayList<>();
        for(AbstractTileMultiblockNode te : tes) {
            tilePoss.add(te.getPos());
        }
        this.target = target;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(tilePoss.size());
        for(BlockPos pos : tilePoss) {
            buf.writeLong(pos.toLong());
        }
        buf.writeBoolean(target);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readByte();
        tilePoss = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            tilePoss.add(BlockPos.fromLong(buf.readLong()));
        }
        target = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<MessageUpdateClientTileMultiblockNodePowered, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateClientTileMultiblockNodePowered message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));

            //return message
            return null;
        }

        private void handle(MessageUpdateClientTileMultiblockNodePowered message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT) {
                Minecraft mc = Minecraft.getMinecraft();
                WorldClient world = mc.world;
                EntityPlayerSP player = mc.player;

                player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Updating clients nodes to " + (message.target ? TextFormatting.DARK_GREEN + "powered" : TextFormatting.DARK_RED + "unpowered")));
                for(BlockPos pos : message.tilePoss) {
                    if (world.isBlockLoaded(pos)) {
                        TileEntity te = world.getTileEntity(pos);

                        if (te instanceof AbstractTileMultiblockNode) {
                            AbstractTileMultiblockNode tile = (AbstractTileMultiblockNode) te;

                            tile.setClientPowered(message.target);

                            //debug
                            player.sendMessage(new TextComponentString( (message.target ? (TextFormatting.GREEN + "[POWERED] ") : (TextFormatting.RED + "[UNPOWERED] "))
                                    + TextFormatting.DARK_AQUA + " Client data updated for "+ TextFormatting.RESET + tile.getClass().getSimpleName()));

                        }
                    }
                }
            }
        }
    }
}
