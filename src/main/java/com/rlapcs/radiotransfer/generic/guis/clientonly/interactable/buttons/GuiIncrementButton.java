package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;

public class GuiIncrementButton extends InteractiveGuiElement {
    public enum IncrementType {
        RIGHT,
        LEFT
    }

    private static final int[] UV = {71, 0};
    private static final int[] DIMS = {5, 8};

    protected IncrementType type;

    public GuiIncrementButton(int id, int x, int y, IncrementType type) {
        super(id, x, y, DIMS[0], DIMS[1]);
        if (type == IncrementType.RIGHT)
            UV[0] = 71;
        else
            UV[0] = 66;
    }

    @Override
    protected int[] getUV() {
        return UV;
    }
}
