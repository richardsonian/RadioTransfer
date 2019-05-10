package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;

public class GuiDumpButton extends InteractiveGuiElement {
    private static final int[] UV = {15, 17};
    private static final int[] DIMS = {21, 11};

    public boolean dumpable;

    public GuiDumpButton(int id, int x, int y, boolean dumpable) {
        super(id, x, y, DIMS[0], DIMS[1]);
        this.dumpable = dumpable;
        UV[1] = dumpable ? 17 : 44;
    }

    @Override
    protected int[] getUV() {
        return UV;
    }
}
