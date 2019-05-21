package com.rlapcs.radiotransfer.network.messages.toClient;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdateClientMultiblockNodeRegistered implements IMessage {
    private BlockPos tilePos;
    private boolean target;

    public MessageUpdateClientMultiblockNodeRegistered() {}
    public MessageUpdateClientMultiblockNodeRegistered(AbstractTileMultiblockNode te, boolean target) {
        tilePos = te.getPos();
        this.target = target;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = BlockPos.fromLong(buf.readLong());
        target = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tilePos.toLong());
        buf.writeBoolean(target);
    }

    public static class Handler implements IMessageHandler<MessageUpdateClientMultiblockNodeRegistered, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateClientMultiblockNodeRegistered message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            //return message
            return null;
        }

        private void handle(MessageUpdateClientMultiblockNodeRegistered message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft mc = Minecraft.getMinecraft();
                WorldClient world = mc.world;
                EntityPlayerSP player = mc.player;

                if (world.isBlockLoaded(message.tilePos)) {
                    TileEntity te = world.getTileEntity(message.tilePos);
                    if (te instanceof AbstractTileMultiblockNode) {
                        AbstractTileMultiblockNode node = (AbstractTileMultiblockNode) te;
                        node.setRegisteredInMultiblock(message.target);
                        player.sendChatMessage(te + " on client now " + (message.target ? "registered" : "deregistered"));
                    }
                }
            }
        }
    }
}
