package com.rlapcs.radiotransfer.generic.multiblock;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import com.rlapcs.radiotransfer.machines.controllers.rx_controller.TileRxController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.TileTxController;
import com.rlapcs.radiotransfer.machines.decoders.abstract_decoder.AbstractTileDecoder;
import com.rlapcs.radiotransfer.machines.encoders.abstract_encoder.AbstractTileEncoder;
import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import com.rlapcs.radiotransfer.server.radio.TransferException;
import com.rlapcs.radiotransfer.server.radio.TransferType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static com.rlapcs.radiotransfer.RadioTransfer.sendDebugMessage;

public class MultiblockRadioController {
    private boolean registeredToNetwork;

    private TileRadio tileEntity;

    private TileTxController txController;
    private TileRxController rxController;

    private TilePowerSupply powerSupply;

    private EnumMap<TransferType, AbstractTileEncoder> encoders;
    private EnumMap<TransferType, AbstractTileDecoder> decoders;

    public MultiblockRadioController(TileRadio te) {
        tileEntity = te;
        encoders = new EnumMap<>(TransferType.class);
        decoders = new EnumMap<>(TransferType.class);
        registeredToNetwork = false;
    }

    public boolean isRegisteredToNetwork(){return registeredToNetwork;}
    public void registerToNetwork() {registeredToNetwork = true;}
    public void deregisterFromNetwork() {registeredToNetwork = false;}

    public int getTransmitFrequency(@Nonnull TransferType type) {
        if(!canTransmit(type)) {
            throw new TransferException(this + " cannot transmit on type " + type);
        }
        else {
            return txController.getFrequency();
        }
    }
    public int getReceiveFrequency(@Nonnull TransferType type) {
        if(!canTransmit(type)) {
            throw new TransferException(this + " cannot receive on type " + type);
        }
        else {
            return rxController.getFrequency();
        }
    }
    public int getTransmitMode(@Nonnull TransferType type) {
        if(!canTransmit(type)) {
            throw new TransferException(this + " cannot transmit on type " + type);
        }
        else {
            return txController.getMode();
        }
    }
    public int getReceivePriority(@Nonnull TransferType type) {
        if(!canTransmit(type)) {
            throw new TransferException(this + " cannot receive on type " + type);
        }
        else {
            return rxController.getFrequency();
        }
    }


    public boolean canTransmit(@Nonnull TransferType type) {
        boolean hasEncoder = encoders.get(type) != null && !encoders.get(type).isInvalid(); //invalid check redundant
        boolean hasTransmitter = txController != null && !txController.isInvalid();

        return hasTransmitter && hasEncoder;
    }
    public boolean canReceive(@Nonnull TransferType type) {
        boolean hasDecoder = decoders.get(type) != null && !decoders.get(type).isInvalid(); //invalid check redundant
        boolean hasReceiver = rxController != null && !rxController.isInvalid();

        return hasDecoder && hasReceiver;
    }
    public <T> T getSendHandler(@Nonnull TransferType type) {
       return null;
    }
    public <T> T getReceiveHandler(@Nonnull TransferType type) {
        return null;
    }

    private boolean validateAddition(BlockPos pos) {
        TileEntity te = tileEntity.getWorld().getTileEntity(pos);

        if(te != null) {
            if (te instanceof AbstractTileMultiblockNode) {
                AbstractTileMultiblockNode node = ((AbstractTileMultiblockNode) te);

                if (!node.isRegisteredInMultiblock()) {
                    if (node instanceof TileTxController) {
                        if (txController == null || txController.isInvalid()) {
                            txController = (TileTxController) node;
                            txController.registerInMultiblock(this);
                            return true;
                        }
                    } else if (node instanceof TileRxController) {
                        if (rxController == null || rxController.isInvalid()) {
                            rxController = (TileRxController) node;
                            rxController.registerInMultiblock(this);
                            return true;
                        }
                    } else if (node instanceof TilePowerSupply) {
                        if (powerSupply == null || powerSupply.isInvalid()) {
                            powerSupply = (TilePowerSupply) node;
                            powerSupply.registerInMultiblock(this);
                            return true;
                        }
                    } else if (node instanceof AbstractTileEncoder) {
                        AbstractTileEncoder encoder = (AbstractTileEncoder) node;
                        TransferType type = encoder.getTransferType();
                        if (encoders.get(type) == null || encoders.get(type).isInvalid()) {
                            encoder.registerInMultiblock(this);
                            encoders.put(type, encoder);
                            return true;
                        }
                    } else if (node instanceof AbstractTileDecoder) {
                        AbstractTileDecoder decoder = (AbstractTileDecoder) node;
                        TransferType type = decoder.getTransferType();
                        if (decoders.get(type) == null || decoders.get(type).isInvalid()) {
                            decoder.registerInMultiblock(this);
                            decoders.put(type, decoder);
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
        else if(node instanceof AbstractTileEncoder) {
            if(encoders.get(((AbstractTileEncoder) node).getTransferType()) == node) encoders.remove(((AbstractTileEncoder) node).getTransferType());
        }
        else if(node instanceof AbstractTileDecoder) {
            if(decoders.get(((AbstractTileDecoder) node).getTransferType()) == node) decoders.remove(((AbstractTileDecoder) node).getTransferType());
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

    public TileRadio getTileEntity() {
        return tileEntity;
    }

    @Override
    public String toString() {
        int x = tileEntity.getPos().getX();
        int y = tileEntity.getPos().getY();
        int z = tileEntity.getPos().getZ();

        return String.format("Multiblock Radio Controller [%d, %d, %d] @%d", x, y, z, hashCode());
    }
}
