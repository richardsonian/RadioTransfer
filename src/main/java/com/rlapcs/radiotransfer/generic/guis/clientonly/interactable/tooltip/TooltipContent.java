package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip;

public class TooltipContent implements ITooltipContent {
    String content;

    public TooltipContent(String content) {
        this.content = content;
    }

    @Override
    public String getFormattedContent() {
        return content;
    }
}
