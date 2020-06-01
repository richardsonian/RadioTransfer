package com.rlapcs.radiotransfer.generic.guis.clientonly.interactable.tooltip;

import com.google.common.collect.Lists;
import scala.actors.threadpool.Arrays;

import java.util.List;

public class TooltipContent implements ITooltipContent {
    private List<String> contentList;

    public TooltipContent(String content) {
        String[] temp = { content };
        this.contentList = Arrays.asList(temp);
    }

    public TooltipContent(List<String> contentList) {
        this.contentList = contentList;
    }

    @Override
    public List<String> getFormattedContent() {
        return contentList;
    }
}
