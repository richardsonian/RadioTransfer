package com.rlapcs.radiotransfer.generic.multiblock;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import com.rlapcs.radiotransfer.machines.controllers.rx_controller.TileRxController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.TileTxController;
import com.rlapcs.radiotransfer.machines.decoders.abstract_decoder.ITileDecoder;
import com.rlapcs.radiotransfer.machines.encoders.abstract_encoder.ITileEncoder;
import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import com.rlapcs.radiotransfer.server.radio.TransferType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class MultiblockRadioController {
    private TileRadio tileEntity;

    private AbstractTileMultiblockNode txController;
    private AbstractTileMultiblockNode rxController;

    private AbstractTileMultiblockNode powerSupply;

    private EnumMap<TransferType, AbstractTileMultiblockNode> encoders;
    private EnumMap<TransferType, AbstractTileMultiblockNode> decoders;

    public MultiblockRadioController(TileRadio te) {
        tileEntity = te;
        encoders = new EnumMap<>(TransferType.class);
        decoders = new EnumMap<>(TransferType.class);
    }
    public TileRadio getTileEntity() {
        return tileEntity;
    }

    public boolean canTransmit(@Nonnull TransferType type) {
        boolean hasEncoder = encoders.get(type) != null && !encoders.get(type).isInvalid(); //invalid check redundant
        //other checks?

        return hasEncoder;
    }

    public boolean canReceive(@Nonnull TransferType type) {
        boolean hasDecoder = decoders.get(type) != null && !decoders.get(type).isInvalid(); //invalid check redundant
        //other checks?

        return hasDecoder;
    }

    public <T> T getSendHandler(@Nonnull TransferType type) {
       return null;
    }

    public <T> T getReceiveHandler(@Nonnull TransferType type) {
        return null;
    }

    public boolean validateAddition(BlockPos pos) {
        TileEntity te = tileEntity.getWorld().getTileEntity(pos);

        if(te != null) {
            if (te instanceof AbstractTileMultiblockNode) {
                AbstractTileMultiblockNode node = ((AbstractTileMultiblockNode) te);

                if (!node.isRegisteredInMultiblock()) {
                    if (node instanceof TileTxController) {
                        if (txController == null || txController.isInvalid()) {
                            txController = node;
                            txController.registerInMultiblock(this);
                            return true;
                        }
                    } else if (node instanceof TileRxController) {
                        if (rxController == null || rxController.isInvalid()) {
                            rxController = node;
                            rxController.registerInMultiblock(this);
                            return true;
                        }
                    } else if (node instanceof TilePowerSupply) {
                        if (powerSupply == null || powerSupply.isInvalid()) {
                            powerSupply = node;
                            powerSupply.registerInMultiblock(this);
                            return true;
                        }
                    } else if (node instanceof ITileEncoder) {
                        TransferType type = ((ITileEncoder) node).getTransferType();
                        if (encoders.get(type) == null || encoders.get(type).isInvalid()) {
                            node.registerInMultiblock(this);
                            encoders.put(type, node);
                            return true;
                        }
                    } else if (node instanceof ITileDecoder) {
                        TransferType type = ((ITileDecoder) node).getTransferType();
                        if (decoders.get(type) == null || decoders.get(type).isInvalid()) {
                            node.registerInMultiblock(this);
                            decoders.put(type, node);
                            return true;
                        }
                    }
                }
            }
        }
        return false; //te not registered
    }
    public void checkForNewNodes(BlockPos around) {
        validateAddition(around.up());
        validateAddition(around.down());
        validateAddition(around.north());
        validateAddition(around.south());
        validateAddition(around.east());
        validateAddition(around.west());
    }

    public void deregisterAllNodes() {
        for(AbstractTileMultiblockNode node : getAllNodes()) {
            if(node != null) {
                node.deregisterFromMultiblock();
                removeNode(node);
            }
        }
    }
    public void removeNode(AbstractTileMultiblockNode node) {
        if(node == txController) txController = null;
        else if(node == rxController) rxController = null;
        else if(node == powerSupply) powerSupply = null;
        else if(node instanceof ITileEncoder) {
            if(encoders.get(((ITileEncoder) node).getTransferType()) == node) encoders.remove(((ITileEncoder) node).getTransferType());
        }
        else if(node instanceof ITileDecoder) {
            if(decoders.get(((ITileDecoder) node).getTransferType()) == node) decoders.remove(((ITileDecoder) node).getTransferType());
        }
    }

    private List<AbstractTileMultiblockNode> getAllNodes() {
        List<AbstractTileMultiblockNode> list = new ArrayList<>();

        list.add(txController);
        list.add(rxController);
        list.add(powerSupply);
        list.addAll(encoders.values());
        list.addAll(decoders.values());

        return list;
    }

    @Override
    public String toString() {
        int x = tileEntity.getPos().getX();
        int y = tileEntity.getPos().getY();
        int z = tileEntity.getPos().getZ();

        return String.format("Multiblock Radio Controller [%d, %d, %d] @%d", x, y, z, hashCode());
    }
}
