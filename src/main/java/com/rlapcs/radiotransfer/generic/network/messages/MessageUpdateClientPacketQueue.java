package com.rlapcs.radiotransfer.generic.network.messages;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class MessageUpdateClientPacketQueue implements IMessage {
    public static class Request implements IMessage {
        private BlockPos tilePos;

        public Request() {}
        public Request(TileEntity te) {
            tilePos = te.getPos();
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            tilePos = BlockPos.fromLong(buf.readLong());
        }
        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeLong(tilePos.toLong());
        }

        public static class Handler implements IMessageHandler<Request, MessageUpdateClientPacketQueue> {
            @Override
            public MessageUpdateClientPacketQueue onMessage(Request message, MessageContext ctx) {
                if(ctx.side == Side.SERVER) {
                    sendDebugMessage("Update packetqueue request received");

                    ListenableFuture<Object> listenable =  FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(
                            () -> getServerTileEntity(message, ctx));

                    final AbstractTileItemProcessor[] te = new AbstractTileItemProcessor[1]; //hack to allow reference in callback
                    Futures.addCallback(listenable, new FutureCallback<Object>() {
                        @Override
                        public void onSuccess(@Nullable Object result) {
                            if(result == null) {
                                onFailure(new NullPointerException("Result was null."));
                            }
                            else if(!(result instanceof AbstractTileItemProcessor)) {
                                onFailure(new RuntimeException("Result is not of right type"));
                            }
                            else {
                                te[0] = (AbstractTileItemProcessor) result;
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            //print error, but just return no response
                            sendDebugMessage("Problem processing client request for packetqueue, sending no response.");
                            t.printStackTrace();
                            te[0] = null;
                        }
                    });

                    //return no message if te is null
                    sendDebugMessage("te null after callback, returning no message");
                    if(te[0] == null) return null;

                    //return message
                    sendDebugMessage("Returning update message!");
                    return new MessageUpdateClientPacketQueue(te[0]);
                }

                sendDebugMessage("Not on server... sending no message back");
                return null;
            }

            private AbstractTileItemProcessor getServerTileEntity(Request message, MessageContext ctx) {
                sendDebugMessage("Getting server tileEntity on game thread.");

                EntityPlayerMP playerEntity = ctx.getServerHandler().player;
                World world = playerEntity.getEntityWorld();

                if (world.isBlockLoaded(message.tilePos)) {
                    sendDebugMessage("tile loaded");
                    TileEntity te = world.getTileEntity(message.tilePos);
                    if(te instanceof AbstractTileItemProcessor) {
                        sendDebugMessage("Instance of AbstractTileItemProcessor; returning te");
                        return (AbstractTileItemProcessor) te;
                    }
                }
                sendDebugMessage("Could not get te; returning null");
                return null;
            }
        }
    }

    private BlockPos tilePos;
    private NBTTagCompound packetNbt;

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = BlockPos.fromLong(buf.readLong());
        packetNbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tilePos.toLong());
        ByteBufUtils.writeTag(buf, packetNbt);
    }

    public MessageUpdateClientPacketQueue() {}

    public MessageUpdateClientPacketQueue(AbstractTileItemProcessor te) {
        tilePos = te.getPos();
        packetNbt = te.getHandler().serializeNBT();
    }

    public static class Handler implements IMessageHandler<MessageUpdateClientPacketQueue, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateClientPacketQueue message, MessageContext ctx) {
            // Always use a construct like this to actually handle your message. This ensures that
            // your 'handle' code is run on the main Minecraft thread. 'onMessage' itself
            // is called on the networking thread so it is not safe to do a lot of things
            // here.
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));

            //return message
            return null;
        }

        private void handle(MessageUpdateClientPacketQueue message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT) {
                World world = Minecraft.getMinecraft().world;

                if (world.isBlockLoaded(message.tilePos)) {
                    TileEntity te = world.getTileEntity(message.tilePos);
                    if (te instanceof AbstractTileItemProcessor) {
                        NBTTagCompound teTags = new NBTTagCompound();
                        teTags.setTag("packets", message.packetNbt);
                        te.readFromNBT(teTags);

                        //debug
                        world.playerEntities.forEach(p -> p.sendMessage(new TextComponentString(
                                TextFormatting.LIGHT_PURPLE + "Updated packet queue for " + TextFormatting.RESET + te)));
                    }
                }
            }
        }
    }
}
