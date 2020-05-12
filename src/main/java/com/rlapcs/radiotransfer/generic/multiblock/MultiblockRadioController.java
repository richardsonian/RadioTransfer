package com.rlapcs.radiotransfer.generic.multiblock;

import com.rlapcs.radiotransfer.generic.capability.ITransferHandler;
import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import com.rlapcs.radiotransfer.machines.controllers.rx_controller.TileRxController;
import com.rlapcs.radiotransfer.machines.controllers.tx_controller.TileTxController;
import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import com.rlapcs.radiotransfer.machines.processors.ProcessorType;
import com.rlapcs.radiotransfer.machines.processors.abstract_processor.AbstractTileProcessor;
import com.rlapcs.radiotransfer.machines.processors.material_processor.AbstractTileMaterialProcessor;
import com.rlapcs.radiotransfer.machines.radio.TileRadio;
import com.rlapcs.radiotransfer.server.radio.RadioNetwork;
import com.rlapcs.radiotransfer.server.radio.TxMode;
import com.rlapcs.radiotransfer.server.radio.UnsupportedTransferException;
import com.rlapcs.radiotransfer.server.radio.TransferType;
import com.rlapcs.radiotransfer.util.Debug;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class MultiblockRadioController {
    //~~~~~~~~~~~~~~~~~~Instance Variables~~~~~~~~~~~~~~~~~~//

    //Multiblock Data
    private boolean registeredToNetwork;
    private boolean multiblockValid;
    private boolean isPowered;
    private MultiblockPowerUsageData powerUsageData;

    //Multiblock Nodes
    private TileRadio tileEntity;

    private TileTxController txController;
    private TileRxController rxController;

    private TilePowerSupply powerSupply;

    private EnumMap<TransferType, AbstractTileProcessor> encoders;
    private EnumMap<TransferType, AbstractTileProcessor> decoders;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public MultiblockRadioController(TileRadio te) {
        tileEntity = te;
        encoders = new EnumMap<>(TransferType.class);
        decoders = new EnumMap<>(TransferType.class);

        registeredToNetwork = false;
        multiblockValid = false;
        isPowered = false;

        powerUsageData = new MultiblockPowerUsageData();
    }

    //##################################################################################################//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~RADIO NETWORK REGISTRY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //##################################################################################################//

    public boolean isRegisteredToNetwork() {
        return registeredToNetwork;
    }
    //server only
    public void registerToNetwork() {
        RadioNetwork.INSTANCE.register(this);
        registeredToNetwork = true;
    }
    //server only
    public void deregisterFromNetwork() {
        RadioNetwork.INSTANCE.deregister(this);
        registeredToNetwork = false;
    }

    //##################################################################################################//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~POWER LOGIC~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //##################################################################################################//

    //---------PowerUsageData Methods-------------//

    //server only [use cached data in TilePowerSupply on client]
    public MultiblockPowerUsageData getPowerUsageData() {
        return powerUsageData;
    }
    //server only
    public void updatePowerUsageData() {
        powerUsageData.updateAll(getAllNodes());
    }

    //-----------Power Logic Methods---------------//

    //vvv Constant Power vvv
    //These are called in TileRadio, in TileEntity#update() cycle
    public int calculateRequiredPowerPerTick() {
        int sum = 0;
        for (AbstractTileMultiblockNode node : getAllNodes()) {
            if(node != null && !node.isInvalid()) {
                sum += node.getPowerPerTick();
            }
        }
        return sum;
    }
    public boolean hasSufficientConstantPower(int ticksSinceLastUpdate) {
        if (powerSupply != null && !powerSupply.isInvalid()) {
            int needed = calculateRequiredPowerPerTick() * ticksSinceLastUpdate;
            int extracted = powerSupply.extractEnergy(needed, true);
            return extracted >= needed;
        }
        return false;
    }
    public boolean useConstantPower(int ticksSinceLastUpdate) {
        if(!hasSufficientConstantPower(ticksSinceLastUpdate)) return false;

        int needed = calculateRequiredPowerPerTick() * ticksSinceLastUpdate;
        int extracted = powerSupply.extractEnergy(needed, false);
        //Debug.sendToAllPlayers(tileEntity + " extracting " + extracted + "FE of power.", tileEntity.getWorld());
        return extracted >= needed; //whether enough power was taken
    }

    //vvv Process Power vvv

    //If this returns false, the caller should call setPowered() to false
    public boolean hasPowerForProcess(int processPower) {
        if(powerSupply != null && !powerSupply.isInvalid()) {
            int extracted = powerSupply.extractEnergy(processPower, true);
            return extracted >= processPower;
        }
        return false;
    }

    //returns whether successful
    public boolean useProcessPower(int processPower) {
        if(!hasPowerForProcess(processPower)) return false;

        int extracted = powerSupply.extractEnergy(processPower, false);
        return extracted >= processPower;
    }
    public boolean useProcessPower(AbstractTileMultiblockNode node) {
        return useProcessPower(node.getPowerPerProcess());
    }

    //~~~~~~~~~~~~~~~~~Powered State~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //These are updated in TileRadio, in TileEntity#update() cycle
    public boolean isPowered() {
        return isPowered;
    }
    /**
     * Side effect!! if set from to false, will empty remaining power.
     * This fixes the scenario where the PSU has a reserve of less than the power required for the next cycle.
     * If set to false, the last bit of power will be emptied
     * @param target
     */
    public void setPowered(boolean target) {
        isPowered = target;
        if(powerSupply != null && powerSupply.hasCapability(CapabilityEnergy.ENERGY, null)) {
            if (!isPowered)
                powerSupply.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(Integer.MAX_VALUE, true);
        }
    }

    //##################################################################################################//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~TRANSMISSION AND RECEIVING~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //##################################################################################################//
    public int getTransmitFrequency(@Nonnull TransferType type) {
        if (!canTransmit(type)) {
            throw new UnsupportedTransferException(this + " cannot transmit on type " + type);
        } else {
            return txController.getFrequency();
        }
    }

    public int getReceiveFrequency(@Nonnull TransferType type) {
        if (!canReceive(type)) {
            throw new UnsupportedTransferException(this + " cannot receive on type " + type);
        } else {
            return rxController.getFrequency();
        }
    }

    public TxMode getTransmitMode(@Nonnull TransferType type) {
        if (!canTransmit(type)) {
            throw new UnsupportedTransferException(this + " cannot transmit on type " + type);
        } else {
            return txController.getMode();
        }
    }

    public int getReceivePriority(@Nonnull TransferType type) {
        if (!canReceive(type)) {
            throw new UnsupportedTransferException(this + " cannot receive on type " + type);
        } else {
            return rxController.getPriority();
        }
    }

    public boolean canTransmit(@Nonnull TransferType type) {
        boolean hasEncoder = encoders.get(type) != null && !encoders.get(type).isInvalid() && !encoders.get(type).getHandler().isEmpty(); //invalid check redundant
        boolean hasTransmitter = txController != null && !txController.isInvalid() && txController.getActivated();

        return hasEncoder && hasTransmitter;
    }

    public boolean canReceive(@Nonnull TransferType type) {
        boolean hasDecoder = decoders.get(type) != null && !decoders.get(type).isInvalid(); //invalid check redundant
        boolean hasReceiver = rxController != null && !rxController.isInvalid() && rxController.getActivated();

        return hasDecoder && hasReceiver;
    }

    //##################################################################################################//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~NODE GETTERS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //##################################################################################################//

    public ITransferHandler getTransmitHandler(@Nonnull TransferType type) {
        if (canTransmit(type)) {
            return encoders.get(type).getHandler();
        } else {
            throw new UnsupportedTransferException(this + " cannot transmit " + type);
        }
    }

    public ITransferHandler getReceiveHandler(@Nonnull TransferType type) {
        if (canReceive(type)) {
            return decoders.get(type).getHandler();
        } else {
            throw new UnsupportedTransferException(this + " cannot receive " + type);
        }
    }

    public AbstractTileProcessor getEncoder(@Nonnull TransferType type) {
        return encoders.get(type);
    }
    public AbstractTileProcessor getDecoder(@Nonnull TransferType type) {
        return decoders.get(type);
    }

    /**
     * LIST MAY RETURN SOME NULLS
     * @return List of all nodes
     */
    private List<AbstractTileMultiblockNode> getAllNodes() {
        List<AbstractTileMultiblockNode> list = new ArrayList<>();

        list.add(txController);
        list.add(rxController);
        list.add(powerSupply);
        list.addAll(encoders.values());
        list.addAll(decoders.values());

        return list;
    }
    //##################################################################################################//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~MULTIBLOCK DETECTION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //##################################################################################################//
    private boolean validateAddition(BlockPos pos) {
        TileEntity te = tileEntity.getWorld().getTileEntity(pos);

        if (te != null) {
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
                    } else if (node instanceof AbstractTileProcessor) {
                        AbstractTileProcessor processor = (AbstractTileProcessor) node;
                        TransferType transferType = processor.getTransferType();
                        ProcessorType processorType = processor.getProcessorType();
                        if (processorType == ProcessorType.ENCODER) {
                            if (encoders.get(transferType) == null || encoders.get(transferType).isInvalid()) {
                                processor.registerInMultiblock(this);
                                encoders.put(transferType, processor);
                                return true;
                            }
                        } else if (processorType == ProcessorType.DECODER) {
                            if (decoders.get(transferType) == null || decoders.get(transferType).isInvalid()) {
                                processor.registerInMultiblock(this);
                                decoders.put(transferType, processor);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false; //tileEntity not registered
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
        for (AbstractTileMultiblockNode node : getAllNodes()) {
            if (node != null) {
                node.deregisterFromMultiblock();
                removeNode(node);
            }
        }
    }

    public void removeNode(AbstractTileMultiblockNode node) {
        if (node == txController) txController = null;
        else if (node == rxController) rxController = null;
        else if (node == powerSupply) powerSupply = null;
        else if (node instanceof AbstractTileProcessor) {
            AbstractTileProcessor processor = (AbstractTileProcessor) node;
            if (processor.getProcessorType() == ProcessorType.ENCODER) {
                if (encoders.get(processor.getTransferType()) == processor) {
                    encoders.remove(processor.getTransferType());
                }
            } else if (processor.getProcessorType() == ProcessorType.DECODER) {
                if (decoders.get(processor.getTransferType()) == processor) {
                    decoders.remove(processor.getTransferType());

                    //update encoder dumpable data when decoder removed
                    AbstractTileProcessor encoder = encoders.get(processor.getTransferType());
                    if(encoder != null && !encoder.isInvalid() && encoder instanceof AbstractTileMaterialProcessor) {
                        ((AbstractTileMaterialProcessor) encoder).doClientDumpableUpdate();
                    }
                }
            }
        }
    }

    //##################################################################################################//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~MISC~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //##################################################################################################//
    public List<BlockPos> getMultiblockPositions() {
        List<AbstractTileMultiblockNode> nodes = this.getAllNodes();
        List<BlockPos> positions = new ArrayList<>();
        positions.add(this.getTileEntity().getPos()); //Add Radio Pos
        //Add node positions
        for (AbstractTileMultiblockNode node : nodes) {
            if (node != null)
                positions.add(node.getPos());
        }
        return positions;
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
