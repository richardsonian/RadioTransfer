package com.rlapcs.radiotransfer.server.radio;

import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.rlapcs.radiotransfer.RadioTransfer.MODID;

public enum RadioRegistry {
    INSTANCE;

    private RadioRegistry() {}

    public static final double[] FREQUENCIES = {1, 2, 3};
}