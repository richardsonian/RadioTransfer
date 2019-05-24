package com.rlapcs.radiotransfer.network.messages.toClient;

import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
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
    private int gainRate;
    private int lossRate;

    public MessageUpdateClientTilePowerData() {}
    public MessageUpdateClientTilePowerData(TilePowerSupply te, int energy, int gainRate, int lossRate) {
        this.tilePos = te.getPos();

        this.energy = energy;
        this.gainRate = gainRate;
        this.lossRate = lossRate;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tilePos.toLong());
        buf.writeInt(energy);
        buf.writeInt(gainRate);
        buf.writeInt(lossRate);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = BlockPos.fromLong(buf.readLong());
        energy = buf.readInt();
        gainRate = buf.readInt();
        lossRate = buf.readInt();
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

                    if(te instanceof TilePowerSupply) {
                        TilePowerSupply tile = (TilePowerSupply) te;

                        tile.setCachedPowerGain(message.gainRate);
                        tile.setCachedPowerUsage(message.lossRate);
                        tile.setDisplayEnergy(message.energy);

                        //debug
                        player.sendMessage(new TextComponentString(TextFormatting.BLUE + String.format("(E:%d, +%d, -%d", message.energy, message.gainRate, message.lossRate) + "Updated client power data for" + TextFormatting.RESET + tile));
                    }
                }
            }
        }
    }
}
