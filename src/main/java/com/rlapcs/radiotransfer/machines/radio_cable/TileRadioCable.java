package com.rlapcs.radiotransfer.machines.radio_cable;

import com.rlapcs.radiotransfer.generic.blocks.IRadioCableConnectable;
import com.rlapcs.radiotransfer.generic.tileEntities.ITileItemHandlerProvider;
import com.rlapcs.radiotransfer.util.Debug;
import com.rlapcs.radiotransfer.util.VecUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TileRadioCable extends TileEntity {
    private Set<EnumFacing> connections;

    public TileRadioCable() {
        connections = EnumSet.noneOf(EnumFacing.class);
    }

    public Set<EnumFacing> getConnections() {
        return connections;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~CONNECTION UPDATE LOGIC~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public void onNeighborChange(BlockPos neighbor) {
        if(updateNeighborConnection(neighbor)) {
            this.markDirty();

            //Send the new connections list to the client
            IBlockState state = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3); //this probably causes some extra updates, not sure
        }
    }

    @Override
    public void onLoad() {
        boolean actionPerformed = false;
        for(EnumFacing facing : EnumFacing.values()) { //Check all surrounding sides
            BlockPos neighbor = this.pos.offset(facing);
            if(updateNeighborConnection(neighbor)) { //for each side, check whether it is a connection
                actionPerformed = true;
            }
        }
        if(actionPerformed) {
            this.markDirty(); //if any connections were added or removed, mark the TE

            //Send the new connections list to the client
            IBlockState state = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3); //this probably causes some extra updates, not sure
        }
    }

    private boolean updateNeighborConnection(BlockPos neighbor) { //This is in its own method so onLoad doesn't have to make multiple needless markDirty() calls
        Block neighborBlock = world.getBlockState(neighbor).getBlock();
        EnumFacing direction = VecUtils.getNeighborDirection(this.pos, neighbor); //Direction from this block to neighbor

        boolean actionPerformed = false;
        if(neighborBlock instanceof IRadioCableConnectable && ((IRadioCableConnectable) neighborBlock).canConnect(direction.getOpposite())){
            if(connections.add(direction)) {
                actionPerformed = true;
                Debug.sendToAllPlayers(String.format("%sCable@(%d, %d, %d) %sconnected %s%s %s(%s)", TextFormatting.GRAY, pos.getX(), pos.getY(), pos.getZ(), TextFormatting.GREEN, TextFormatting.YELLOW, direction.getName2(), TextFormatting.RESET, (world.isRemote ? "client" : "server")), world);
            }
        }
        else {
            if(connections.remove(direction)) {
                actionPerformed = true;
                Debug.sendToAllPlayers(String.format("%sCable@(%d, %d, %d) %sdisconnected %s%s %s(%s)", TextFormatting.GRAY, pos.getX(), pos.getY(), pos.getZ(), TextFormatting.RED, TextFormatting.YELLOW, direction.getName2(), TextFormatting.RESET, (world.isRemote ? "client" : "server")), world);
            }
        }

        //end debug
        return actionPerformed;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~NETWORKING & NBT CACHING~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~copied from AbstractTileMachine~~~~~~~~~//
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("connections")) {
            int[] serialized_connections = compound.getIntArray("connections");
            connections = EnumSet.copyOf(IntStream.of(serialized_connections).mapToObj(EnumFacing::getFront).collect(Collectors.toList()));
        }
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        int[] serialized_connections = connections.stream().mapToInt(EnumFacing::getIndex).toArray();
        compound.setIntArray("connections", serialized_connections);
        return compound;
    }
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = getUpdateTag();
        int metadata = getBlockMetadata();
        return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
    }
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
        // can also use metadata here (dont)
    }
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }
    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }
    //~~~~~~~~end copied from AbstractTileMachine~~~~~~~~//

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~MISC~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Override
    public String toString() {
        return String.format("%s at [%d, %d, %d]", this.getClass().getSimpleName(), pos.getX(), pos.getY(), pos.getZ());
    }
}
