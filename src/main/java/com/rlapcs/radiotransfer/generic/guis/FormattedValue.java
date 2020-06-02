package com.rlapcs.radiotransfer.generic.guis;

public class FormattedValue {
    private double value;
    private String order;
    private String unit;

    public FormattedValue(double value, String order, String unit) {
        this.value = value;
        this.order = order;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public String getOrder() {
        return order;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        if (value == Math.floor(value) && !Double.isInfinite(value))
            return (int) value + " " + order + unit;
        return value + " " + order + unit;
    }
}
