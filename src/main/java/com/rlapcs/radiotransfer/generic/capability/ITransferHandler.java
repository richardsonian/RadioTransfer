package com.rlapcs.radiotransfer.generic.capability;

public interface ITransferHandler {
    boolean isEmpty();
    void onContentsChanged();
    void onLoad();
}
