package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.generic.guis.coordinate.CoordinateXY;
import com.rlapcs.radiotransfer.generic.guis.coordinate.DimensionWidthHeight;

public interface IGui {
    CoordinateXY getGuiPos();
    DimensionWidthHeight getGuiSize();
}
