package com.rlapcs.radiotransfer.server.radio;

public enum TxMode {
    ROUND_ROBIN,
    SEQUENTIAL;

    public static TxMode getNext(TxMode current) {
        if(current.ordinal() < values().length - 1) {
            return values()[current.ordinal() + 1];
        }
        else {
            return values()[0];
        }
    }
}
