package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;

public class GuiIncrementButton extends InteractiveGuiElement {
    public enum IncrementType {
        RIGHT,
        LEFT
    }

    protected IncrementType type;

    public GuiIncrementButton(int id, int x, int y, IncrementType type) {
        super(id, x, y, 5, 8);
        iconV = 0;
        if (type == IncrementType.RIGHT) {
            iconU = 71;
            iconV = 0;
        } else {
            iconU = 66;
            iconV = 0;
        }
    }
}
