package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.lists;

import com.rlapcs.radiotransfer.generic.capability.ItemPacketQueue;
import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.items.PowerSupplyListItem;
import com.rlapcs.radiotransfer.machines.power_supply.TilePowerSupply;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class PowerSupplyList extends AbstractGuiList {
    public PowerSupplyList(Minecraft mc, GuiScreen screen, ItemPacketQueue queue, int x, int y, int guiLeft, int guiTop, TilePowerSupply tile) {
        super(mc, screen, queue, x, y, guiLeft, guiTop);
        for (int i = 0; i < NUM_ITEMS; i++)
            items.add(new PowerSupplyListItem(i, guiLeft + x, guiTop + y + i * 18, i, tile));
    }
}
