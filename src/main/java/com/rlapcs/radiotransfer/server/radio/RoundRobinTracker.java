package com.rlapcs.radiotransfer.server.radio;

import java.util.List;

public class RoundRobinTracker<T> {
    private int index;
    private List<T> destinations;

    public RoundRobinTracker(List<T> destinations) {
        index = 0;
        this.destinations = destinations;
    }

    public boolean isComplete() {
        return index >= destinations.size() - 1;
    }

    public T getNext() {
        T element = destinations.get(index);
        index++;
        return element;
    }
}
