package com.rlapcs.radiotransfer.network.messages.toClient;

import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdateClientTileMultiblockPowerData implements IMessage {
    private BlockPos tilePos;
    private NBTTagCompound powerDataNBT;

    public MessageUpdateClientTileMultiblockPowerData() {}
    public MessageUpdateClientTileMultiblockPowerData(TilePowerSupply te) {
        this.tilePos = te.getPos();
        this.powerDataNBT = te.getController().getPowerUsageData().serializeNBT();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tilePos.toLong());
        ByteBufUtils.writeTag(buf, powerDataNBT);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = BlockPos.fromLong(buf.readLong());
        powerDataNBT = ByteBufUtils.readTag(buf);
    }

    public static class Handler implements IMessageHandler<MessageUpdateClientTileMultiblockPowerData, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateClientTileMultiblockPowerData message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));

            //return message
            return null;
        }

        private void handle(MessageUpdateClientTileMultiblockPowerData message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT) {
                Minecraft mc = Minecraft.getMinecraft();
                WorldClient world = mc.world;
                EntityPlayerSP player = mc.player;

                if(world.isBlockLoaded(message.tilePos)) {
                    TileEntity te = world.getTileEntity(message.tilePos);

                    if(te instanceof TilePowerSupply) {
                        TilePowerSupply tile = (TilePowerSupply) te;

                        tile.getCachedPowerUsageData().deserializeNBT(message.powerDataNBT);

                        //debug
                        //player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Updated client power data for " + TextFormatting.RESET + tile));
                    }
                }
            }
        }
    }
}
