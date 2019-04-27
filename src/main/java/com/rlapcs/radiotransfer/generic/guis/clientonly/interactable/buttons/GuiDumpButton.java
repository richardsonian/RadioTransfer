package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;

public class GuiDumpButton extends InteractiveGuiElement {
    public boolean dumpable;

    public GuiDumpButton(int id, int x, int y, boolean dumpable) {
        super(id, x, y, 21, 11);
        this.dumpable = dumpable;
        iconV = 15;
        iconU = dumpable ? 17 : 44;
    }
}
