package com.rlapcs.radiotransfer.generic.multiblock.tileEntities;

import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

import static com.rlapcs.radiotransfer.RadioTransfer.sendDebugMessage;

public abstract class AbstractTileMultiblockNode extends AbstractTileMachine {
    public static final int MULTIBLOCK_UPDATE_TICKS = 20;

    protected boolean registeredInMultiblock;
    protected MultiblockRadioController controller;

    public AbstractTileMultiblockNode() {
        registeredInMultiblock = false;
    }

    public void registerInMultiblock(MultiblockRadioController controller) {
        this.controller = controller;
        registeredInMultiblock = true;
        sendDebugMessage(this + " registered to: " + controller + (registeredInMultiblock ? "(registered)" : "(unregistered)"));
    }

    public void deregisterFromMultiblock() {
        sendDebugMessage(this + " deregistered from: " + controller);
        controller.removeNode(this);
        this.controller = null;
        registeredInMultiblock = false;

        this.notifySurroundingDetatch();
    }
    public boolean isRegisteredInMultiblock() {
        return registeredInMultiblock;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if(!world.isRemote) {
            if(registeredInMultiblock) {
                this.deregisterFromMultiblock();
            }
        }
    }

    private boolean isAdjacentToController() {
        BlockPos[] neighbors = {pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()};
        for(BlockPos n : neighbors) {
            TileEntity te = world.getTileEntity(n);
            if(te instanceof TileRadio) {
                if(((TileRadio) te).getMultiblockController() == controller) {
                    sendDebugMessage(this + " is adjacent to " + controller);
                    return true;
                }
            }
        }
        return false;
    }
    private List<AbstractTileMultiblockNode> getNeighboringNodes() {
        List<AbstractTileMultiblockNode> nodes = new ArrayList<>();

        BlockPos[] neighbors = {pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()};
        for(BlockPos n : neighbors) {
            TileEntity te = world.getTileEntity(n);
            if(te instanceof AbstractTileMultiblockNode) {
                sendDebugMessage("found neighboring multiblock tile " + te + (registeredInMultiblock ? "(registered)" : "(deregistered)")+ " with controller" + ((AbstractTileMultiblockNode) te).getController());
                if(((AbstractTileMultiblockNode) te).isRegisteredInMultiblock() && ((AbstractTileMultiblockNode) te).getController() == this.getController()) {
                    sendDebugMessage("adding " + te + "to neighbor list");
                    nodes.add((AbstractTileMultiblockNode) te);
                }
            }
        }
        sendDebugMessage("found neighboring nodes: " + nodes);
        return nodes;
    }
    private boolean isConnectedToController() {
        if(this.isAdjacentToController()) return true;
        else {
            for(AbstractTileMultiblockNode node : getNeighboringNodes()) {
                if(node.isConnectedToController()) {
                    sendDebugMessage(this + " is connected to its controller");
                    return true;
                }
            }
            return false;
        }
    }
    private void notifySurroundingDetatch() {
        for(AbstractTileMultiblockNode node : getNeighboringNodes()) {
            sendDebugMessage(this + " notified detatch to " + node);
            verifyConnectedToController();
        }
    }
    private void verifyConnectedToController() {
        if(registeredInMultiblock) {
            if(!isConnectedToController()) {
                this.deregisterFromMultiblock();
            }
        }
    }

    public MultiblockRadioController getController() {
        return controller;
    }

    public abstract double getPowerUsagePerTick();

    @Override
    public void update() {
        super.update();
        if(!world.isRemote) {
            if (this.registeredInMultiblock && ticksSinceCreation % MULTIBLOCK_UPDATE_TICKS == 0) {
                controller.checkForNewNodes(this.pos);
            }
        }
    }
}
