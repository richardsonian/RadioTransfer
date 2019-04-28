package com.rlapcs.radiotransfer.machines.decoders.abstract_decoder;

import com.rlapcs.radiotransfer.server.radio.TransferType;

public interface ITileDecoder {
    TransferType getTransferType();
}
