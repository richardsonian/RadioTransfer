package com.rlapcs.radiotransfer.generic.network.messages.toClient;

import com.rlapcs.radiotransfer.machines.processors.item_processors.abstract_item_processor.AbstractTileItemProcessor;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdateClientPacketQueue implements IMessage {
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
                Minecraft mc = Minecraft.getMinecraft();
                WorldClient world = mc.world;
                EntityPlayerSP player = mc.player;

                if (world.isBlockLoaded(message.tilePos)) {
                    TileEntity te = world.getTileEntity(message.tilePos);
                    if (te instanceof AbstractTileItemProcessor) {
                        AbstractTileItemProcessor tile = (AbstractTileItemProcessor) te;
                        tile.getHandler().deserializeNBT(message.packetNbt);

                        //debug
                        player.sendMessage(new TextComponentString(
                                TextFormatting.LIGHT_PURPLE + "Updated packet queue for " + TextFormatting.RESET + te));
                    }
                }
                else {
                    player.sendMessage(new TextComponentString(TextFormatting.DARK_RED + "Tileentity not loaded on client"));
                }
            }
        }
    }
}
