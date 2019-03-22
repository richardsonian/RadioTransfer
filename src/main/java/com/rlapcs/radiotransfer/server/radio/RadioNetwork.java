package com.rlapcs.radiotransfer.server.radio;

import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.rlapcs.radiotransfer.RadioTransfer.MODID;

public class RadioNetwork extends WorldSavedData {
    public static final double[] FREQUENCIES = {1, 2, 3};

    private static final String DATA_NAME = MODID + "_ExampleData";

    private static int ID = 0;
    private static Map<Node, NodeType> nodes = new HashMap<>();

    public RadioNetwork() {
        super(DATA_NAME);
    }
    public RadioNetwork(String s) {
        super(s);
    }

    public static void addNode(String callsign, NodeType type, double frequency) {
        nodes.put(new Node(callsign, type, frequency, ID++), type);
    }

    public static void handshake(Node firstNode, Node secondNode) {
        firstNode.connectWith(secondNode);
        secondNode.connectWith(firstNode);
    }
}