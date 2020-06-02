package com.rlapcs.radiotransfer.network.messages.toClient;

import com.rlapcs.radiotransfer.machines.radio.TileRadio;
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

public class MessageUpdateClientRadioPowered implements IMessage {
    private BlockPos tilePos;
    private boolean target;

    public MessageUpdateClientRadioPowered() {}
    public MessageUpdateClientRadioPowered(TileRadio te, boolean target) {
        tilePos = te.getPos();
        this.target = target;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tilePos.toLong());
        buf.writeBoolean(target);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = BlockPos.fromLong(buf.readLong());
        target = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<MessageUpdateClientRadioPowered, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateClientRadioPowered message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));

            //return message
            return null;
        }

        private void handle(MessageUpdateClientRadioPowered message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT) {
                Minecraft mc = Minecraft.getMinecraft();
                WorldClient world = mc.world;
                EntityPlayerSP player = mc.player;

                player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Updated client radio to " + (message.target ? TextFormatting.DARK_GREEN + "powered" : TextFormatting.DARK_RED + "unpowered")));
                if (world.isBlockLoaded(message.tilePos)) {
                    TileEntity te = world.getTileEntity(message.tilePos);
                    if (te instanceof TileRadio) {
                        TileRadio tile = (TileRadio) te;

                        tile.setClientPowered(message.target);

                    }
                }
            }
        }
    }
}