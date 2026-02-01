package io.github.nx.LunaX.engine.net;

public interface NetObject {
    int getNetID();
    int getTypeID(); 
    float getX();
    float getY();
    
    void onNetworkUpdate(Object[] data);
}