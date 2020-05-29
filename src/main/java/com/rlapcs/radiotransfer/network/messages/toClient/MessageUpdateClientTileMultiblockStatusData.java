package com.rlapcs.radiotransfer.network.messages.toClient;

import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdateClientTileMultiblockStatusData implements IMessage {
    private BlockPos controllerPos;
    private NBTTagCompound statusNBT;

    public MessageUpdateClientTileMultiblockStatusData() {}
    public MessageUpdateClientTileMultiblockStatusData(BlockPos controllerPos, NBTTagCompound statusNBT) {
        this.controllerPos = controllerPos;
        this.statusNBT = statusNBT;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(controllerPos.toLong());
        ByteBufUtils.writeTag(buf, statusNBT);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        controllerPos = BlockPos.fromLong(buf.readLong());
        statusNBT = ByteBufUtils.readTag(buf);
    }

    public static class Handler implements IMessageHandler<MessageUpdateClientTileMultiblockStatusData, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateClientTileMultiblockStatusData message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));

            //return message
            return null;
        }

        private void handle(MessageUpdateClientTileMultiblockStatusData message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT) {
                Minecraft mc = Minecraft.getMinecraft();
                WorldClient world = mc.world;
                EntityPlayerSP player = mc.player;

                if(world.isBlockLoaded(message.controllerPos)) {
                    TileEntity te = world.getTileEntity(message.controllerPos);

                    if(te instanceof TileRadio) {
                        TileRadio tile = (TileRadio) te;

                        if(message.statusNBT.hasKey("node_status_list")) {
                            NBTTagList tagList = message.statusNBT.getTagList("node_status_list", Constants.NBT.TAG_COMPOUND);
                            for(int i = 0; i < tagList.tagCount(); i++) {
                                NBTTagCompound nbt = (NBTTagCompound) tagList.get(i);
                                tile.getMultiblockStatusData().readNodeFromNBT(nbt);
                            }
                        }
                        else {
                            tile.getMultiblockStatusData().readNodeFromNBT(message.statusNBT);
                        }

                        //debug
                        //player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Updated client power data for " + TextFormatting.RESET + tile));
                    }
                }
            }
        }
    }
}
