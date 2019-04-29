package com.rlapcs.radiotransfer.generic.multiblock.tileEntities;

import com.rlapcs.radiotransfer.generic.multiblock.MultiblockRadioController;
import com.rlapcs.radiotransfer.generic.tileEntities.AbstractTileMachine;
import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

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
        sendDebugMessage(TextFormatting.GREEN + " " + this + " registered " + TextFormatting.RESET + " to: " + controller );
    }

    public void deregisterFromMultiblock() {
        sendDebugMessage(TextFormatting.RED + " " + this + " deregistered " + TextFormatting.RESET + " from: " + controller);
        registeredInMultiblock = false; //must come before notify surrounding
        this.notifySurroundingDetatch();
        controller.removeNode(this);
        this.controller = null; //must come after notify surrounding
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

    /**
     * Called when the chunk this TileEntity is on is Unloaded.
     */
    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
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
                    //sendDebugMessage(this + " is adjacent to " + controller);
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
                AbstractTileMultiblockNode node = (AbstractTileMultiblockNode) te;
                //sendDebugMessage("found neighboring multiblock tile " + node + (node.isRegisteredInMultiblock() ? "(registered)" : "(deregistered)")+ " with controller" + node.getController());
                if(node.isRegisteredInMultiblock() && (node.getController() == this.getController())) {
                    //sendDebugMessage("adding " + node + "to neighbor list");
                    nodes.add(node);
                }
            }
        }
        //sendDebugMessage("found neighboring nodes: " + nodes);
        return nodes;
    }
    private boolean isConnectedToController(List<AbstractTileMultiblockNode> alreadyBeingChecked) { //recursion :)
        if(this.isAdjacentToController()) return true;
        else {
            //so that it doesn't contain nodes already asked if they're already asking
            alreadyBeingChecked.add(this);
            List<AbstractTileMultiblockNode> nodes = getNeighboringNodes();
            nodes.removeAll(alreadyBeingChecked);

            for(AbstractTileMultiblockNode node : nodes) {
                if(node.isConnectedToController(alreadyBeingChecked)) {
                    //sendDebugMessage(this + " is connected to its controller");
                    return true;
                }
            }
            return false;
        }
    }
    private void notifySurroundingDetatch() {
        for(AbstractTileMultiblockNode node : getNeighboringNodes()) {
            //sendDebugMessage(this + " notified detatch to " + node);
            node.verifyConnectedToController();
        }
    }
    private void verifyConnectedToController() {
        if(registeredInMultiblock) {
            if(!isConnectedToController(new ArrayList<>())) {
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
