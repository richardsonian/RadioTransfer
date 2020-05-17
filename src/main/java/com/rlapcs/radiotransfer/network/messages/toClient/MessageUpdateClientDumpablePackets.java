package com.rlapcs.radiotransfer.network.messages.toClient;

import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
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

import java.util.Arrays;

public class MessageUpdateClientDumpablePackets implements IMessage {
    private BlockPos tilePos;
    private byte dataSize;
    private boolean[] data;

    public MessageUpdateClientDumpablePackets() {}
    public MessageUpdateClientDumpablePackets(AbstractTileMaterialProcessor te, boolean[] data) {
        this.tilePos = te.getPos();
        this.data = data;
        this.dataSize = (byte) data.length;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = BlockPos.fromLong(buf.readLong());
        dataSize = buf.readByte();
        data = new boolean[dataSize];
        for(int i = 0; i < dataSize; i++) {
            data[i] = buf.readBoolean();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tilePos.toLong());
        buf.writeByte(dataSize);
        for(int i = 0; i < dataSize; i++) {
            buf.writeBoolean(data[i]);
        }
    }

    public static class Handler implements IMessageHandler<MessageUpdateClientDumpablePackets, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateClientDumpablePackets message, MessageContext ctx) {
            // Always use a construct like this to actually handle your message. This ensures that
            // your 'handle' code is run on the main Minecraft thread. 'onMessage' itself
            // is called on the networking thread so it is not safe to do a lot of things
            // here.
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));

            //return message
            return null;
        }

        private void handle(MessageUpdateClientDumpablePackets message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT) {
                Minecraft mc = Minecraft.getMinecraft();
                WorldClient world = mc.world;
                EntityPlayerSP player = mc.player;

                if (world.isBlockLoaded(message.tilePos)) {
                    TileEntity te = world.getTileEntity(message.tilePos);
                    if (te instanceof AbstractTileMaterialProcessor) {
                        AbstractTileMaterialProcessor tile = (AbstractTileMaterialProcessor) te;
                        tile.setDumpableData(message.data);
                        //debug
                        /*
                        player.sendMessage(new TextComponentString(
                                TextFormatting.LIGHT_PURPLE + "Updated dumpable data for " + TextFormatting.RESET + te));
                        player.sendMessage(new TextComponentString(
                                TextFormatting.GRAY + Arrays.toString(message.data)
                        ));
                        */
                    }
                }
                else {
                    //player.sendMessage(new TextComponentString(TextFormatting.DARK_RED + "Tileentity not loaded on client"));
                }
            }
        }
    }
}
