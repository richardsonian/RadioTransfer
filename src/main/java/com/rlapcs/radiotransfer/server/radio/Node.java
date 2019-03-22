package com.rlapcs.radiotransfer.server.radio;

import java.util.ArrayList;

public class Node {
    private String callsign;
    private NodeType type;
    private ArrayList<Node> connections;
    private double frequency;
    private int ID;

    public Node(String callsign, NodeType type, double frequency, int ID) {
        this.callsign = callsign;
        this.type = type;
        this.frequency = frequency;
        this.ID = ID;
        connections = new ArrayList<>();
    }

    public void changeFrequency(double newFrequency) {
        frequency = newFrequency;
    }

    public void connectWith(Node node) {
        connections.add(node);
    }
}
