package com.rlapcs.radiotransfer.server.radio;

public enum TransferType{
    ITEM("Item"), FLUID("Fluid"), POWER("Power"), ME("ME");

    private String friendlyName;
    private TransferType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
