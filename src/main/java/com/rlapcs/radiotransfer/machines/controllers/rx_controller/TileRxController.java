package com.rlapcs.radiotransfer.machines.controllers.rx_controller;

import com.rlapcs.radiotransfer.machines.controllers.abstract_controller.AbstractTileController;
import net.minecraft.util.math.MathHelper;

import static com.rlapcs.radiotransfer.server.radio.RadioNetwork.MAX_PRIORITY;
import static com.rlapcs.radiotransfer.server.radio.RadioNetwork.MIN_PRIORITY;

public class TileRxController extends AbstractTileController {

    private int priority = 0;

    public TileRxController() {
        super();
    }

    public void changePriority(boolean toIncrement) {
        priority = MathHelper.clamp(getFrequency() + (toIncrement ? 1 : -1), MIN_PRIORITY, MAX_PRIORITY);
    }

    public int getPriority() {
        return priority;
    }
}
