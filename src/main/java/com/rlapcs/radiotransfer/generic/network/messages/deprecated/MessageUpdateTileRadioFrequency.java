package com.rlapcs.radiotransfer.generic.network.messages.deprecated;

import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileRadio;
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

public class MessageUpdateTileRadioFrequency implements IMessage {
    private BlockPos tilePos;
    private boolean toIncrement; //true if going to increment, false if going to decrement

    @Override
    public void fromBytes(ByteBuf buf) {
        // Encoding the position as a long is more efficient
        tilePos = BlockPos.fromLong(buf.readLong());
        toIncrement = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // Encoding the position as a long is more efficient
        buf.writeLong(tilePos.toLong());
        buf.writeBoolean(toIncrement);
    }

    public MessageUpdateTileRadioFrequency() {}

    public MessageUpdateTileRadioFrequency(TileEntity te, Boolean toIncrement ) {
        tilePos = te.getPos();
        this.toIncrement = toIncrement;
    }

    public static class Handler implements IMessageHandler<MessageUpdateTileRadioFrequency, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateTileRadioFrequency message, MessageContext ctx) {
            // Always use a construct like this to actually handle your message. This ensures that
            // your 'handle' code is run on the main Minecraft thread. 'onMessage' itself
            // is called on the networking thread so it is not safe to do a lot of things
            // here.
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));

            //return message
            return null;
        }

        private void handle(MessageUpdateTileRadioFrequency message, MessageContext ctx) {
            // This code is run on the server side. So you can do server-side calculations here
            EntityPlayerMP playerEntity = ctx.getServerHandler().player;
            World world = playerEntity.getEntityWorld();

            // Check if the block (chunk) is loaded to prevent abuse from a client
            // trying to overload a server by randomly loading chunks
            if (world.isBlockLoaded(message.tilePos)) {
                TileEntity te = world.getTileEntity(message.tilePos);
                if(te instanceof AbstractTileRadio) {
                    ((AbstractTileRadio) te).changeFrequency(message.toIncrement);
                }
                //debug
                playerEntity.sendStatusMessage(new TextComponentString(String.format("%s Tile Entity at (%d, %d, %d) is now on frequency %d", TextFormatting.GREEN,
                        message.tilePos.getX(), message.tilePos.getY(), message.tilePos.getZ(), ((AbstractTileRadio) te).getFrequency() ) ), false);
            }
        }
    }
}
