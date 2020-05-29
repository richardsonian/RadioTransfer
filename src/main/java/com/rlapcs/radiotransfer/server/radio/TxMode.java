package com.rlapcs.radiotransfer.server.radio;

public enum TxMode {
    ROUND_ROBIN("Round Robin"),
    SEQUENTIAL("Sequential");

    private String friendlyName;

    private TxMode(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public static TxMode getNext(TxMode current) {
        if(current.ordinal() < values().length - 1) {
            return values()[current.ordinal() + 1];
        }
        else {
            return values()[0];
        }
    }
}
