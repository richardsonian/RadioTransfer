package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;
import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateUV;

public class GuiIncrementButton extends InteractiveGuiElement {
    public enum IncrementType {
        RIGHT,
        LEFT
    }

    private static final int[] DIMS = {5, 8};

    private int[] UV;

    public GuiIncrementButton(int id, int x, int y, IncrementType type) {
        super(id, x, y, DIMS[0], DIMS[1]);
        UV = new int[2];
        if (type == IncrementType.RIGHT)
            UV[0] = 71;
        else
            UV[0] = 66;
    }

    @Override
    protected CoordinateUV getUV() {
        return new CoordinateUV(UV[0], UV[1]); //hotfix (sorry, too lazy to refactor this class to CoordinateUV system rn)
    }
}
