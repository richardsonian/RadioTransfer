package com.rlapcs.radiotransfer.generic.network.messages;

import com.rlapcs.radiotransfer.machines.controllers.tx_controller.TileTxController;
import com.rlapcs.radiotransfer.server.radio.TxMode;
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

public class MessageChangeTileTxControllerMode implements IMessage {
    private BlockPos tilePos;
    private TxMode targetMode;

    @Override
    public void fromBytes(ByteBuf buf) {
        // Encoding the position as a long is more efficient
        tilePos = BlockPos.fromLong(buf.readLong());
        targetMode = TxMode.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // Encoding the position as a long is more efficient
        buf.writeLong(tilePos.toLong());
        buf.writeInt(targetMode.ordinal());
    }

    public MessageChangeTileTxControllerMode() {}
    public MessageChangeTileTxControllerMode(TileTxController te) {
        tilePos = te.getPos();
        targetMode = TxMode.getNext(te.getMode());
    }

    public static class Handler implements IMessageHandler<MessageChangeTileTxControllerMode, IMessage> {
        @Override
        public IMessage onMessage(MessageChangeTileTxControllerMode message, MessageContext ctx) {
            // Always use a construct like this to actually handle your message. This ensures that
            // your 'handle' code is run on the main Minecraft thread. 'onMessage' itself
            // is called on the networking thread so it is not safe to do a lot of things
            // here.
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));

            //return message
            return null;
        }

        private void handle(MessageChangeTileTxControllerMode message, MessageContext ctx) {
            // This code is run on the server side. So you can do server-side calculations here
            EntityPlayerMP playerEntity = ctx.getServerHandler().player;
            World world = playerEntity.getEntityWorld();

            // Check if the block (chunk) is loaded to prevent abuse from a client
            // trying to overload a server by randomly loading chunks
            if (world.isBlockLoaded(message.tilePos)) {
                TileEntity te = world.getTileEntity(message.tilePos);
                if(te instanceof TileTxController) {
                    ((TileTxController) te).changeMode();

                    //debug
                    playerEntity.sendStatusMessage(new TextComponentString(String.format("%s Tile Entity at (%d, %d, %d) is now on mode %s", TextFormatting.GREEN,
                            message.tilePos.getX(), message.tilePos.getY(), message.tilePos.getZ(), ((TileTxController) te).getMode()) ), false);
                }
            }
        }
    }
}