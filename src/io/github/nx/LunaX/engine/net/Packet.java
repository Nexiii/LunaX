package io.github.nx.LunaX.engine.net;

import java.io.Serializable;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public int netID;
    
    public Object[] data;

    public Packet(int netID, Object[] data) {
        this.netID = netID;
        this.data = data;
    }
}