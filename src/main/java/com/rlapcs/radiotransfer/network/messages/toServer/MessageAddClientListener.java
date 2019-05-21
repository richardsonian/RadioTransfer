package com.rlapcs.radiotransfer.network.messages.toServer;

import com.rlapcs.radiotransfer.network.messages.toClient.MessageUpdateClientPacketQueue;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileClientUpdater;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageAddClientListener implements IMessage{
    private BlockPos tilePos;
    private boolean toAdd;

    public MessageAddClientListener() {}
    public MessageAddClientListener(TileEntity te, boolean toAdd) {
        if(!(te instanceof ITileClientUpdater)) throw new IllegalArgumentException("tile entity must implement ITileClientUpdater");
        tilePos = te.getPos();
        this.toAdd = toAdd;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = BlockPos.fromLong(buf.readLong());
        toAdd = buf.readBoolean();
    }
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tilePos.toLong());
        buf.writeBoolean(toAdd);
    }

    public static class Handler implements IMessageHandler<MessageAddClientListener, IMessage> {
        @Override
        public MessageUpdateClientPacketQueue onMessage(MessageAddClientListener message, MessageContext ctx) {
            if(ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(
                        () -> handle(message, ctx));

            }

            return null;
        }

        private void handle(MessageAddClientListener message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            World world = player.getEntityWorld();

            /*
            player.sendStatusMessage(new TextComponentString(TextFormatting.AQUA + "Server received request to " +
                    (message.toAdd ? "add" : "remove") + " from clientListeners."), false);
             */

            if (world.isBlockLoaded(message.tilePos)) {
                TileEntity te = world.getTileEntity(message.tilePos);
                if(te instanceof ITileClientUpdater) {
                    ITileClientUpdater tile = (ITileClientUpdater) te;
                    //player.sendStatusMessage(new TextComponentString(TextFormatting.GRAY + "Found te to update: " + te), false);
                    if(message.toAdd) {
                        tile.addClientListener(player);
                    }
                    else {
                        tile.removeClientListener(player);
                    }
                }
            }
            else {
                player.sendMessage(new TextComponentString(TextFormatting.DARK_RED + "Tileentity not loaded on server"));
            }
        }
    }
}
