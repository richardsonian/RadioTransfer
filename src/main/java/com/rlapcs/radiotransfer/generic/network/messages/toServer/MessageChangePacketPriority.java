package com.rlapcs.radiotransfer.generic.network.messages.toServer;

import com.rlapcs.radiotransfer.generic.capability.IMaterialTransferHandler;
import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.network.messages.toClient.MessageUpdateClientPacketQueue;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageChangePacketPriority implements IMessage {
    private BlockPos tilePos;
    private int toSwitch;

    public MessageChangePacketPriority() {
    }

    public MessageChangePacketPriority(AbstractTileMaterialProcessor te, int toSwitch) {
        tilePos = te.getPos();
        this.toSwitch = toSwitch;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = BlockPos.fromLong(buf.readLong());
        toSwitch = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tilePos.toLong());
        buf.writeInt(toSwitch);
    }

    public static class Handler implements IMessageHandler<MessageChangePacketPriority, IMessage> {
        @Override
        public IMessage onMessage(MessageChangePacketPriority message, MessageContext ctx) {

            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));

            return null;
        }

        private void handle(MessageChangePacketPriority message, MessageContext ctx) {
            EntityPlayerMP playerEntity = ctx.getServerHandler().player;
            World world = playerEntity.getEntityWorld();

            AbstractTileMaterialProcessor tile = (AbstractTileMaterialProcessor) world.getTileEntity(message.tilePos);
            IMaterialTransferHandler handler = tile.getHandler();
            handler.move(message.toSwitch, message.toSwitch - 1);
        }
    }
}
