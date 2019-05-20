package com.rlapcs.radiotransfer.generic.network.messages.toServer;

import com.rlapcs.radiotransfer.generic.capability.IMaterialTransferHandler;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageDumpItemFromQueue implements IMessage {
    private BlockPos tilePos;
    private int index;

    public MessageDumpItemFromQueue() {
    }

    public MessageDumpItemFromQueue(BlockPos tilePos, int index) {
        this.tilePos = tilePos;
        this.index = index;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = BlockPos.fromLong(buf.readLong());
        index = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tilePos.toLong());
        buf.writeInt(index);
    }

    public static class Handler implements IMessageHandler<MessageDumpItemFromQueue, IMessage> {
        @Override
        public IMessage onMessage(MessageDumpItemFromQueue message, MessageContext ctx) {

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));

            return null;
        }

        private void handle(MessageDumpItemFromQueue message, MessageContext ctx) {
            EntityPlayerMP playerEntity = ctx.getServerHandler().player;
            World world = playerEntity.getEntityWorld();

            AbstractTileMaterialProcessor tile = (AbstractTileMaterialProcessor) world.getTileEntity(message.tilePos);
            IMaterialTransferHandler handler = tile.getHandler();
        }
    }
}
