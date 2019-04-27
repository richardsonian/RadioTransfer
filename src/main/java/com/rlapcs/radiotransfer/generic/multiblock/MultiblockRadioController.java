package com.rlapcs.radiotransfer.generic.multiblock;

import com.rlapcs.radiotransfer.generic.multiblock.tileEntities.AbstractTileMultiblockNode;
import com.rlapcs.radiotransfer.server.radio.TransferType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class MultiblockRadioController {
    public boolean canTransmit(TransferType type) {
        return false;
    }

    public boolean canReceive(TransferType type) {
        return false;
    }

    public <T> T getSendHandler(TransferType type) {
        return null;
    }

    public <T> T getReceiveHandler(TransferType type) {
        return null;
    }

    public void validateCurrentMultiblockNodes() {
    
    }

    public void validateMultiblockAddition(TileEntity te) {

    }
}
