package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.buttons;

import com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.InteractiveGuiElement;
import com.rlapcs.radiotransfer.generic.network.messages.toServer.MessageChangePacketPriority;
import com.rlapcs.radiotransfer.generic.network.messages.toServer.MessageDumpItemFromQueue;
import com.rlapcs.radiotransfer.registries.ModNetworkMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Mouse;

public class GuiDumpButton extends InteractiveGuiElement {
    private static final int[] UV = {17, 15};
    private static final int[] DIMS = {27, 11};

    public boolean dumpable, clicking;

    public GuiDumpButton(int id, int x, int y, boolean dumpable) {
        super(id, x, y, DIMS[0], DIMS[1]);
        this.dumpable = dumpable;
        UV[0] = dumpable ? 17 : 44;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks, int index, BlockPos tilePos) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        boolean flag = Mouse.isButtonDown(0);
        if (this.hovered && flag && !clicking)
            ModNetworkMessages.INSTANCE.sendToServer(new MessageDumpItemFromQueue(tilePos, index));
        clicking = flag;
    }

    @Override
    protected int[] getUV() {
        return UV;
    }
}
