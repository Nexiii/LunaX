package io.github.nx.LunaX.engine.net;

public interface GamePacketListener {
    void onSystemPacket(String type, Object[] data);
}