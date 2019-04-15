package com.rlapcs.radiotransfer.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSendKey implements IMessage {
    private BlockPos blockPos;

    @Override
    public void fromBytes(ByteBuf buf) {
        // Encoding the position as a long is more efficient
        blockPos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // Encoding the position as a long is more efficient
        buf.writeLong(blockPos.toLong());
    }

    /**
     * Gets block position.
     * @return block position
     */
    public BlockPos getBlockPos() {
        return blockPos;
    }

    public PacketSendKey() {
        // Calculate the position of the block we are looking at
        blockPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
    }
}