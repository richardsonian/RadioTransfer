package com.rlapcs.radiotransfer.network.messages.toClient;

import com.rlapcs.radiotransfer.generic.tileEntities.ITilePowerBarProvider;
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

public class MessageUpdateClientTilePowerData implements IMessage {
    private BlockPos tilePos;
    private int energy;
    private double gainRate;
    private double lossRate;

    public MessageUpdateClientTilePowerData() {}
    public MessageUpdateClientTilePowerData(TileEntity te, int energy, double gainRate, double lossRate) {
        if(!(te instanceof ITilePowerBarProvider)) throw new IllegalArgumentException("TE must implement ITilePowerBarProvider");

        this.tilePos = te.getPos();

        this.energy = energy;
        this.gainRate = gainRate;
        this.lossRate = lossRate;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tilePos.toLong());
        buf.writeInt(energy);
        buf.writeDouble(gainRate);
        buf.writeDouble(lossRate);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = BlockPos.fromLong(buf.readLong());
        energy = buf.readInt();
        gainRate = buf.readDouble();
        lossRate = buf.readDouble();
    }

    public static class Handler implements IMessageHandler<MessageUpdateClientTilePowerData, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateClientTilePowerData message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));

            //return message
            return null;
        }

        private void handle(MessageUpdateClientTilePowerData message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT) {
                Minecraft mc = Minecraft.getMinecraft();
                WorldClient world = mc.world;
                EntityPlayerSP player = mc.player;

                if(world.isBlockLoaded(message.tilePos)) {
                    TileEntity te = world.getTileEntity(message.tilePos);

                    if(te instanceof ITilePowerBarProvider) {
                        ITilePowerBarProvider tile = (ITilePowerBarProvider) te;

                        tile.setCachedEnergyGain(message.gainRate);
                        tile.setCachedEnergyUsage(message.lossRate);
                        tile.setDisplayEnergy(message.energy);

                        //debug
                        player.sendMessage(new TextComponentString(TextFormatting.BLUE + String.format("(%dFE, +%f, -%f", message.energy, message.gainRate, message.lossRate) + "Updated client power data for" + TextFormatting.RESET + tile));
                    }
                }
            }
        }
    }
}
